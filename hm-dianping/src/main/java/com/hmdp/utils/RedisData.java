package com.hmdp.utils;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData<DataType> {
    private LocalDateTime expireTime;
    private DataType data;
}
