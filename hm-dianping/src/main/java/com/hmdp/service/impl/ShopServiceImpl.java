package com.hmdp.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.CacheClient;
import com.hmdp.utils.RedisData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Resource
    private CacheClient cacheClient;

    @Override
    public Result getShopInfo(Long id) {
        // 1. 使用空对象解决缓存穿透问题
//        Shop shop = cacheClient.queryWithPassThrough(
//                CACHE_SHOP_KEY,
//                id,
//                Shop.class,
//                this::getById,
//                CACHE_NULL_TTL,
//                TimeUnit.MINUTES
//        );

        // 2. 使用redis自带的setnx解决缓存击穿问题
        // Shop shop = getShopInfoUserSetnxForSolveGoThrough(id);

        // 3. 使用逻辑过期解决缓存击穿问题
        Shop shop = cacheClient.queryWithLogicalExpire(
                CACHE_SHOP_KEY,
                id,
                Shop.class,
                CACHE_SHOP_TTL,
                TimeUnit.SECONDS,
                LOCK_SHOP_KEY,
                LOCK_SHOP_TTL,
                TimeUnit.SECONDS,
                this::getById
        );
        if (shop == null) {
            return Result.fail("商铺不存在");
        }
        return Result.ok(shop);
    }

    // 1. 使用空对象解决缓存穿透问题
//    private Shop getShopInfoUseNullForSolvePassThrough(Long id) {
//        String key = CACHE_SHOP_KEY + id;
//        // 1. 查询缓存是否存在商铺
//        String shopJson = stringRedisTemplate.opsForValue().get(key);
//        // 2. 如果存在，直接返回
//        if (StringUtils.isNotBlank(shopJson)) {
//            Shop shopInCache = JSONUtil.toBean(shopJson, Shop.class);
//            return shopInCache;
//        }
//        if (shopJson != null) {
//            return null;
//        }
//        // 3. 如果不存在，查询数据库，写入缓存，设置过期时间
//        Shop shopInStore = this.getById(id);
//        if (shopInStore == null) {
//            // 4. 如果数据库也不存在，返回空
//            // 4.1 写空值
//            stringRedisTemplate.opsForValue().set(key, "", CACHE_SHOP_TTL, TimeUnit.MINUTES);
//            return null;
//        }
//        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopInStore), CACHE_SHOP_TTL, TimeUnit.MINUTES);
//        return shopInStore;
//    }

    // 2. 使用互斥锁解决缓存击穿问题
//    private Shop getShopInfoUserSetnxForSolveGoThrough(Long id) {
//        String key = CACHE_SHOP_KEY + id;
//        // 1. 查询缓存是否存在商铺
//        String shopJson = stringRedisTemplate.opsForValue().get(key);
//        // 2. 如果存在，直接返回
//        if (StringUtils.isNotBlank(shopJson)) {
//            return JSONUtil.toBean(shopJson, Shop.class);
//        }
//        // 2.1 如果是空字符串，直接返回
//        // 解决缓存穿透问题
//        if (shopJson != null) {
//            return null;
//        }
//        // 3. 获取锁对象
//        // 解决缓存击穿问题
//        Boolean lock = getLock(LOCK_SHOP_KEY);
//        try {
//            if (Boolean.FALSE.equals(lock)) {
//                Thread.sleep(50);
//                return getShopInfoUserSetnxForSolveGoThrough(id);
//            } else {
//                // 模拟延时
//                Thread.sleep(1000);
//                // 4. 如果不存在，查询数据库，写入缓存，设置过期时间
//                Shop shopInStore = this.getById(id);
//                // 解决缓存穿透问题
//                if (shopInStore == null) {
//                    // 4. 如果数据库也不存在，返回空
//                    // 4.1 写空值
//                    stringRedisTemplate.opsForValue().set(key, "", CACHE_SHOP_TTL, TimeUnit.MINUTES);
//                    return null;
//                }
//                stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shopInStore), CACHE_SHOP_TTL, TimeUnit.MINUTES);
//                return shopInStore;
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            // 5. 释放锁
//            unlock(LOCK_SHOP_KEY);
//        }
//    }

    // 3. 使用逻辑过期解决缓存击穿问题
//    private Shop getShopInfoLogicalExpireForSolveGoThrough(Long id) {
//        String key = CACHE_SHOP_KEY + id;
//        // 1. 查询缓存是否存在商铺
//        String shopJson = stringRedisTemplate.opsForValue().get(key);
//        // 2. 如果不存在，则直接返回
//        if (StringUtils.isBlank(shopJson)) {
//            return null;
//        }
//        // 3. 如果存在，判断是否过期
//        RedisData redisData = JSONUtil.toBean(shopJson, RedisData.class);
//        if (redisData.getExpireTime().isAfter(LocalDateTime.now())) {
//            // 4. 如果没有过期，直接返回
//            JSONObject data = (JSONObject) redisData.getData();
//            Shop shop = JSONUtil.toBean(data, Shop.class);
//            return shop;
//        }
//        // 5. 如果过期，获取锁对象
//        Boolean lock = getLock(LOCK_SHOP_KEY + id);
//        try {
//            // 5.1 如果获取锁失败，直接返回
//            if (Boolean.FALSE.equals(lock)) {
//                return null;
//            }
//            // 5.2 如果获取锁成功, 返回旧数据，异步更新数据
//            JSONObject data = (JSONObject) redisData.getData();
//            Shop shop = JSONUtil.toBean(data, Shop.class);
//            // 5.3 异步更新数据
//            executorService.submit(() -> {
//                try {
//                    saveShopToRedis(key, shop, 30L);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//            return shop;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        } finally {
//            // 5.4 释放锁
//            unlock(LOCK_SHOP_KEY);
//        }
//    }

    @Override
    @Transactional
    public Result updateShop(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("商铺id不能为空");
        }
        // 1. 更新数据库
        this.updateById(shop);
        // 2. 删除缓存
        String key = CACHE_SHOP_KEY + shop.getId();
        stringRedisTemplate.delete(key);
        return Result.ok();
    }

    // 获取锁对象
    public Boolean getLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", LOCK_SHOP_TTL, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    // 释放对象
    public Boolean unlock(String key) {
        Boolean flag = stringRedisTemplate.delete(key);
        return BooleanUtil.isTrue(flag);
    }

    // 保存数据到Redis中，设置过期时间（预热处理）
    public void saveShopToRedis(String key, Shop shop, Long expireTime) throws InterruptedException {
        RedisData redisData = new RedisData();
        Thread.sleep(200);
        redisData.setData(shop);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }
}
