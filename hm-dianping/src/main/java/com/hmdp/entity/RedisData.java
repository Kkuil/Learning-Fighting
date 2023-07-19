package com.hmdp.entity;

import lombok.Data;

@Data
public class RedisData<DataType> {
    private String expire;
    private DataType data;
}
