package com.hmdp.service.impl;

import cn.hutool.core.lang.UUID;
import com.hmdp.service.ILockService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


public class LockServiceImpl implements ILockService {

    // 锁名称
    private String name;
    // redis操作对象
    private StringRedisTemplate stringRedisTemplate;
    // 锁名称前缀
    private static final String LOCK_PREFIX = "lock:";
    // 锁值前缀
    private static final String LOCK_VALUE_PREFIX = UUID.randomUUID().toString(true) + "-";

    private static DefaultRedisScript REDIS_SCRIPT;

    static {
        // 初始化脚本
        REDIS_SCRIPT = new DefaultRedisScript<Long>();
        // 初始化脚本位置
        REDIS_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        // 初始化返回类型
        REDIS_SCRIPT.setResultType(Long.class);
    }

    public LockServiceImpl(String name, StringRedisTemplate stringRedisTemplate) {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean getLock(Long expireTime) {
        String key = LOCK_PREFIX + name;
        // 使用UUID标识服务 + 服务内当前线程的id作为锁的值(防止超时，苏醒误删别人的锁)
        String value = LOCK_VALUE_PREFIX + Thread.currentThread().getId();
        Boolean isLock = this.stringRedisTemplate
                .opsForValue()
                .setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(isLock);
    }

    @Override
    public void unLock() {
        // 这样就可以保证原子性
        String key = LOCK_PREFIX + name;
        String value = LOCK_VALUE_PREFIX + Thread.currentThread().getId();
        this.stringRedisTemplate.execute(REDIS_SCRIPT, Collections.singletonList(key), value);
    }

//    @Override
//    public void unLock() {
//        // 获取自己的锁值
//        String value = LOCK_VALUE_PREFIX + Thread.currentThread().getId();
//        // redis中的锁值
//        String redisValue = this.stringRedisTemplate.opsForValue().get(LOCK_PREFIX + name);
//        // 判断锁值是否相等(防止超时，苏醒误删别人的锁)
//        // 在这里存在线程安全问题，如果当当前线程判断完进入删除锁的业务的时候阻塞住了，
//        // 这个业务又很长，导致锁超时释放了，这个时候其他线程进来获取锁了，并且能够成功获取锁，
//        // 这个时候就出现了当前线程能删除锁，并且其他线程也能获取到锁的情况，就又有可能出现误删的情况
//        // 为什么会出现这种情况？就是因为判断和删除是两个操作，不能保证原子性
//        // 这个时候就需要使用能保证原子性的语言Lua脚本了
//        if (value.equals(redisValue)) {
//            // 假设这里有特别多的业务，导致锁超时释放了，
//            // 但是此时已经拿到了删除锁的权限，其他线程进入加的锁，就会被当前线程误删......
//            // 相等则删除锁
//            this.stringRedisTemplate.delete(LOCK_PREFIX + name);
//        }
//    }
}
