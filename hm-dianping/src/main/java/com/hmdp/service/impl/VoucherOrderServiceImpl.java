package com.hmdp.service.impl;

import com.hmdp.dto.Result;
import com.hmdp.entity.SeckillVoucher;
import com.hmdp.entity.User;
import com.hmdp.entity.VoucherOrder;
import com.hmdp.mapper.VoucherOrderMapper;
import com.hmdp.service.ISeckillVoucherService;
import com.hmdp.service.IVoucherOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.RedisUtil;
import com.hmdp.utils.UserHolder;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements IVoucherOrderService {

    @Resource
    private ISeckillVoucherService seckillVoucherService;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public Result seckillVoucher(Long voucherId) {
        // 1. 判断秒杀券是否存在
        SeckillVoucher voucher = seckillVoucherService.getById(voucherId);
        if (voucher == null) {
            return Result.fail("秒杀券不存在");
        }
        // 2. 判断秒杀券是否在秒杀有效期内
        if (voucher.getBeginTime().isAfter(voucher.getEndTime())) {
            return Result.fail("秒杀尚未开始");
        }
        if (voucher.getEndTime().isBefore(voucher.getBeginTime())) {
            return Result.fail("秒杀已经结束");
        }
        // 3. 判断秒杀券是否还有库存
        if (voucher.getStock() < 0) {
            return Result.fail("秒杀券已经被抢光了");
        }
        Long userId = UserHolder.getUser().getId();
        synchronized (userId.toString().intern()) {
            IVoucherOrderService voucherOrderService = (IVoucherOrderService) AopContext.currentProxy();
            return voucherOrderService.createVoucher(userId, voucherId);
        }
    }

    @Transactional
    @Override
    public Result createVoucher(Long userId, Long voucherId) {
        // 4. 一人只能秒杀一张
        boolean isExist = this.query()
                .eq("user_id", userId)
                .eq("voucher_id", voucherId)
                .count() > 0;
        if (isExist) {
            return Result.fail("您已经秒杀过了");
        }
        // 5. 扣减库存(类版本号法)
        boolean isSucUpdate = seckillVoucherService
                .update()
                .setSql("stock = stock - 1")
                .eq("voucher_id", voucherId)
                .gt("stock", 0)
                .update();
        if (!isSucUpdate) {
            return Result.fail("秒杀失败");
        }
        // 6. 生成秒杀订单
        VoucherOrder voucherOrder = new VoucherOrder();
        // 6.1 生成订单ID
        Long orderId = redisUtil.getUniqueId("order");
        voucherOrder.setId(orderId);
        // 6.2 用户ID
        voucherOrder.setUserId(userId);
        // 6.3 秒杀券ID
        voucherOrder.setVoucherId(voucherId);
        boolean isSucOrder = this.save(voucherOrder);
        // 7. 返回结果
        if (isSucOrder) {
            return Result.ok(orderId);
        } else {
            return Result.fail("秒杀失败");
        }
    }
}
