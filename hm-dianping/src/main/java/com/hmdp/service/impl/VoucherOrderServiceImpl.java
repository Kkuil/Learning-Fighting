package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.hmdp.config.RedissonClientConfig;
import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ILockService;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisUtil;
import com.hmdp.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.aop.framework.AopContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
@Slf4j
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClientConfig redissonClientConfig;

    // 秒杀脚本
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    // 代理
    private IVoucherOrderService proxy;

    // 初始化秒杀脚本对象
    static {
        // 初始化脚本
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        // 初始化脚本位置
        SECKILL_SCRIPT.setLocation(new ClassPathResource("seckill.lua"));
        // 初始化返回类型
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    // 1. 异步下单，创建线程池
    private static final ExecutorService SECKILL_ORDER_EXECUTOR = Executors.newSingleThreadExecutor();


    // 初始化执行器，让线程加载执行器
    // PostConstruct注解为了保证在构造函数后执行初始化
    @PostConstruct
    private void init() {
        SECKILL_ORDER_EXECUTOR.submit(new VoucherOrderHandler());
    }

    private class VoucherOrderHandler implements Runnable {

        // 订单队列名称
        private static final String QUEUE_NAME = "stream.orders";
        // 消费者名称
        private static final String CONSUMER_NAME = "order_consumer";
        // 订单组名
        public static final String GROUP_NAME = "g1";
        // 最长阻塞时间(单位毫秒)
        public static final long MAX_BLOCK_TIME = 2000;

        @Override
        public void run() {
            while (true) {
                try {
                    // 1. 从Redis消息队列中获取消息
                    List<MapRecord<String, Object, Object>> message_queue = stringRedisTemplate.opsForStream().read(
                            Consumer.from(GROUP_NAME, CONSUMER_NAME),
                            StreamReadOptions.empty().count(1).block(Duration.ofMillis(MAX_BLOCK_TIME)),
                            StreamOffset.create(QUEUE_NAME, ReadOffset.lastConsumed())
                    );
                    // 2. 判断是否有消息
                    if (message_queue == null || message_queue.size() == 0) {
                        // 2.1 没消息进入下一次循环，继续阻塞
                        continue;
                    }
                    // 2.2 有消息，尝试消费消息
                    MapRecord<String, Object, Object> message = message_queue.get(0);
                    Map<Object, Object> value = message.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(value, new VoucherOrder(), true);
                    // 3. 创建订单
                    handleVoucherOrder(voucherOrder);
                    // 4. ACK确认消息
                    stringRedisTemplate.opsForStream().acknowledge("s1", GROUP_NAME, message.getId());
                } catch (Exception e) {
                    log.error("订单异常");
                    // 处理pending消息
                    handlePendingOrder();
                }
            }
        }

        private void handlePendingOrder() {
            while (true) {
                try {
                    // 1. 从Redis消息队列中获取消息
                    List<MapRecord<String, Object, Object>> message_queue = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", CONSUMER_NAME),
                            StreamReadOptions.empty().count(1),
                            StreamOffset.create(QUEUE_NAME, ReadOffset.lastConsumed())
                    );
                    // 2. 判断是否有消息
                    if (message_queue == null || message_queue.size() == 0) {
                        // 2.1 没消息进入下一次循环，结束阻塞
                        break;
                    }
                    // 2.2 有消息，尝试消费消息
                    MapRecord<String, Object, Object> message = message_queue.get(0);
                    Map<Object, Object> value = message.getValue();
                    VoucherOrder voucherOrder = BeanUtil.fillBeanWithMap(value, new VoucherOrder(), true);
                    // 3. 创建订单
                    handleVoucherOrder(voucherOrder);
                    // 4. ACK确认消息
                    stringRedisTemplate.opsForStream().acknowledge("s1", "g1", message.getId());
                } catch (Exception e) {
                    log.error("订单异常");
                }
            }
        }
    }

    // 3. 订单处理
    private void handleVoucherOrder(VoucherOrder voucherOrder) {
        Long userId = voucherOrder.getUserId();
        RLock lockService = redissonClientConfig.redissonClient().getLock("lock:order:" + userId);
        boolean isLock = lockService.tryLock();
        if (!isLock) {
            log.error("下单失败");
        }
        try {
            proxy.createVoucher(voucherOrder);
        } finally {
            // 释放锁
            lockService.unlock();
        }
    }

    // 使用异步阻塞队列实现秒杀下单
    @Override
    public Result seckillVoucher(Long voucherId) {
        // 1. 获取用户Id
        Long userId = UserHolder.getUser().getId();
        // 2. 获取订单ID
        Long orderId = redisUtil.getUniqueId("order");
        // 3. 执行脚本
        Long result = this.stringRedisTemplate.execute(
                SECKILL_SCRIPT,
                Collections.emptyList(),
                voucherId.toString(),
                userId.toString(),
                orderId.toString()
        );
        // 4. 判断是否下单成功
        int res = result.intValue();
        if (res != 0) {
            return Result.fail(res == 1 ? "库存不足" : "不能重复下单");
        }
        // 5. 初始化代理对象
        proxy = (IVoucherOrderService) AopContext.currentProxy();
        // 6. 返回结果
        return Result.ok(orderId);
    }

    @Transactional
    @Override
    public void createVoucher(VoucherOrder voucherOrder) {
        Long voucherId = voucherOrder.getVoucherId();
        Long userId = voucherOrder.getUserId();
        // 1. 一人只能秒杀一张
        boolean isExist = this.query()
                .eq("user_id", userId)
                .eq("voucher_id", voucherId)
                .count() > 0;
        if (isExist) {
            log.error("您已经秒杀过了");
        }
        // 2. 扣减库存(类版本号法)
        boolean isSucUpdate = seckillVoucherService
                .update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId)
                .gt("stock", 0)
                .update();
        if (!isSucUpdate) {
            log.error("秒杀失败");
        }
        // 3. 保存数据到数据库中
        this.save(voucherOrder);
    }

    // 2. 初始化阻塞队列
//    private BlockingQueue<VoucherOrder> orderTasks = new ArrayBlockingQueue<>(1024 * 1024);
    // 3. 异步下单的执行器
//    private class VoucherOrderHandler implements Runnable {
//
//        @Override
//        public void run() {
//            // 不断总阻塞队列中取任务（创建订单）进行执行
//            while (true) {
//                try {
//                    // 1. 获取队列中的订单
//                    VoucherOrder voucherOrder = orderTasks.take();
//                    // 2. 执行创建订单
//                    handleVoucherOrder(voucherOrder);
//                } catch (Exception e) {
//                    log.error("处理订单异常", e);
//                }
//            }
//        }
//    }
    // 使用异步阻塞队列实现秒杀下单
//    @Override
//    public Result seckillVoucher(Long voucherId) {
//        // 获取用户Id
//        Long userId = UserHolder.getUser().getId();
//        // 1. 执行脚本
//        Long result = this.stringRedisTemplate.execute(
//                SECKILL_SCRIPT,
//                Collections.emptyList(),
//                voucherId.toString(),
//                userId.toString()
//        );
//        // 2. 判断是否下单成功
//        int res = result.intValue();
//        if (res != 0) {
//            return Result.fail(res == 1 ? "库存不足" : "不能重复下单");
//        }
//        VoucherOrder voucherOrder = new VoucherOrder();
//        // 3.1.订单id
//        long orderId = redisUtil.getUniqueId("order");
//        voucherOrder.setId(orderId);
//        // 3.2.用户id
//        voucherOrder.setUserId(userId);
//        // 3.3.代金券id
//        voucherOrder.setVoucherId(voucherId);
//        // 3.4.放入阻塞队列
//        orderTasks.add(voucherOrder);
//
//        // 4. 初始化代理对象
//        proxy = (IVoucherOrderService) AopContext.currentProxy();
//        // 5. 返回结果
//        return Result.ok(orderId);
//    }

    // 不使用异步实现秒杀下单
//    @Override
//    public Result seckillVoucher(Long voucherId) {
//        // 1. 判断秒杀券是否存在
//        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
//        if (voucher == null) {
//            return Result.fail("秒杀券不存在");
//        }
//        // 2. 判断秒杀券是否在秒杀有效期内
//        if (voucher.getBeginTime().isAfter(voucher.getEndTime())) {
//            return Result.fail("秒杀尚未开始");
//        }
//        if (voucher.getEndTime().isBefore(voucher.getBeginTime())) {
//            return Result.fail("秒杀已经结束");
//        }
//        // 3. 判断秒杀券是否还有库存
//        if (voucher.getStock() < 0) {
//            return Result.fail("秒杀券已经被抢光了");
//        }
//        Long userId = UserHolder.getUser().getId();
//        // 附加：获取锁对象
//        // LockServiceImpl lockService = new LockServiceImpl("order:" + userId, stringRedisTemplate);
//        RLock lockService = redissonClientConfig.redissonClient().getLock("lock:order:" + userId);
//        boolean isLock = lockService.tryLock();
//        if (!isLock) {
//            return Result.fail("系统繁忙，请稍后再试");
//        }
//        try {
//            IVoucherOrderService voucherOrderService = (IVoucherOrderService) AopContext.currentProxy();
//            return voucherOrderService.createVoucher(userId, voucherId);
//        } finally {
//            // 释放锁
//            lockService.unlock();
//        }
//    }
//
    //    @Transactional
//    @Override
//    public Result createVoucher(Long userId, Long voucherId) {
//        // 4. 一人只能秒杀一张
//        boolean isExist = this.query()
//                .eq("user_id", userId)
//                .eq("voucher_id", voucherId)
//                .count() > 0;
//        if (isExist) {
//            return Result.fail("您已经秒杀过了");
//        }
//        // 5. 扣减库存(类版本号法)
//        boolean isSucUpdate = seckillVoucherService
//                .update()
//                .setSql("stock = stock - 1")
//                .eq("voucher_id", voucherId)
//                .gt("stock", 0)
//                .update();
//        if (!isSucUpdate) {
//            return Result.fail("秒杀失败");
//        }
//        // 6. 生成秒杀订单
//        VoucherOrder voucherOrder = new VoucherOrder();
//        // 6.1 生成订单ID
//        Long orderId = redisUtil.getUniqueId("order");
//        voucherOrder.setId(orderId);
//        // 6.2 用户ID
//        voucherOrder.setUserId(userId);
//        // 6.3 秒杀券ID
//        voucherOrder.setVoucherId(voucherId);
//        boolean isSucOrder = this.save(voucherOrder);
//        // 7. 返回结果
//        if (isSucOrder) {
//            return Result.ok(orderId);
//        } else {
//            return Result.fail("秒杀失败");
//        }
//    }
}
