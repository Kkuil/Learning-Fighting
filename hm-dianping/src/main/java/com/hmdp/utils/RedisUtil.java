package com.hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.hmdp.utils.RedisConstants.BEGIN_TIME_SECOND;
import static com.hmdp.utils.RedisConstants.OFFSET_BIT;

/**
 * @Author 小K
 * @Date 2023/7/20 8:00
 * @Desc redis工具类
 */
@Component
public class RedisUtil {

    private StringRedisTemplate stringRedisTemplate;

    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Long getUniqueId(String key) {
        // 1. 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long offsetSecond = nowSecond - BEGIN_TIME_SECOND;

        // 2. 生成自增序号(在进行redis key生成时，需要添加上当天的日期作为key的后缀)
        String dateKeySuffix = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = stringRedisTemplate.opsForValue().increment("icr:" + key + ":" + dateKeySuffix);

        // 3. 拼接成唯一id(左移+或运算)
        return count << OFFSET_BIT | offsetSecond;
    }
}
