package com.hmdp.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hmdp.entity.Shop;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.hmdp.utils.RedisConstants.*;
import static com.hmdp.utils.RedisConstants.LOCK_SHOP_KEY;

@Component
public class CacheClient {

    private StringRedisTemplate stringRedisTemplate;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 设置缓存
     *
     * @param key      缓存key
     * @param value    缓存的值
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    public void set(String key, Object value, Long ttl, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), ttl, timeUnit);
    }

    /**
     * 设置逻辑过期
     *
     * @param key      缓存key
     * @param value    缓存的值
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    public void setLogicalExpire(String key, Object value, Long ttl, TimeUnit timeUnit) {
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(ttl)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisData));
    }

    /**
     * 使用空对象解决缓存穿透
     *
     * @param keyPrefix  缓存key前缀
     * @param id         需要查询的id
     * @param type       需要转换的类型
     * @param doCallback 查询数据库的回调
     * @param ttl        过期时间
     * @param timeUnit   时间单位
     * @param <R>        返回类型
     * @param <ID>       id类型
     * @return 返回查询结果
     */
    public <R, ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID, R> doCallback, Long ttl, TimeUnit timeUnit) {
        String key = keyPrefix + id;
        // 1. 查询缓存是否存在商铺
        String json = stringRedisTemplate.opsForValue().get(key);
        // 2. 如果存在，直接返回
        if (StringUtils.isNotBlank(json)) {
            return JSONUtil.toBean(json, type);
        }
        if (json != null) {
            return null;
        }
        // 3. 如果不存在，查询数据库，写入缓存，设置过期时间
        R r = doCallback.apply(id);
        if (r == null) {
            // 4. 如果数据库也不存在，返回空
            // 4.1 写空值
            stringRedisTemplate.opsForValue().set(key, "", ttl, timeUnit);
            return null;
        }
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(r), CACHE_SHOP_TTL, TimeUnit.MINUTES);
        return r;
    }

    /**
     * 使用逻辑过期解决缓存击穿
     *
     * @param keyPrefix  缓存key前缀
     * @param id         需要查询的id
     * @param type       需要转换的类型
     * @param dataTtl    数据过期时间
     * @param dataUnit   数据过期时间单位
     * @param lockKey    锁key
     * @param lockTtl    锁过期时间
     * @param lockUnit   锁过期时间单位
     * @param doCallback 查询数据库的回调
     * @param <R>        返回类型
     * @param <ID>       id类型
     * @return 返回查询结果
     */
    public <R, ID> R queryWithLogicalExpire(
            String keyPrefix,
            ID id,
            Class<R> type,
            Long dataTtl,
            TimeUnit dataUnit,
            String lockKey,
            Long lockTtl,
            TimeUnit lockUnit,
            Function<ID, R> doCallback
    ) {
        String key = keyPrefix + id;
        // 1. 查询缓存是否存在
        String json = stringRedisTemplate.opsForValue().get(key);
        // 2. 如果不存在，则直接返回
        if (StringUtils.isBlank(json)) {
            return null;
        }
        // 3. 如果存在，判断是否过期
        RedisData redisData = JSONUtil.toBean(json, RedisData.class);
        if (redisData.getExpireTime().isAfter(LocalDateTime.now())) {
            // 4. 如果没有过期，直接返回
            return JSONUtil.toBean((JSONObject) redisData.getData(), type);
        }
        // 5. 如果过期，获取锁对象
        Boolean lock = getLock(lockKey + id, lockTtl, lockUnit);
        try {
            // 5.1 如果获取锁失败，直接返回
            if (Boolean.FALSE.equals(lock)) {
                return JSONUtil.toBean((JSONObject) redisData.getData(), type);
            }
            // 5.2 如果获取锁成功, 返回旧数据，异步更新数据
            JSONObject data = (JSONObject) redisData.getData();
            R r = JSONUtil.toBean(data, type);
            // 5.3 异步更新数据
            executorService.submit(() -> {
                setLogicalExpire(key, doCallback.apply(id), dataTtl, dataUnit);
            });
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 5.4 释放锁
            unlock(lockKey);
        }
    }

    // 获取锁对象
    public Boolean getLock(String key, Long ttl, TimeUnit timeUnit) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", ttl, timeUnit);
        return BooleanUtil.isTrue(flag);
    }

    // 释放对象
    public Boolean unlock(String key) {
        Boolean flag = stringRedisTemplate.delete(key);
        return BooleanUtil.isTrue(flag);
    }
}
