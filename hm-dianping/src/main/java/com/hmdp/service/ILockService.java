package com.hmdp.service;

public interface ILockService {
    /**
     * 获取锁
     *
     * @param expireTime 锁过期时间
     */
    public boolean getLock(Long expireTime);

    /**
     * 释放锁
     */
    public void unLock();
}
