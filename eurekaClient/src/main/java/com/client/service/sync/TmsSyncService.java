package com.client.service.sync;

import com.songshu.snt.autobots.DTO.TmsLockDTO;

import java.util.function.Supplier;

/**
 * @author weifeng
 * @description Tms同步服务
 * @date 2019/3/28
 */
public interface TmsSyncService {
    /**
     * 通过redis加锁
     * @param supplier
     * @return
     */
    boolean getLockByRedis(Supplier<TmsLockDTO> supplier);

    /**
     * 并发执行操作
     * @param tmsLockDTO
     * @param supplier
     * @return
     */
    Object concurrentSafeInvoke(TmsLockDTO tmsLockDTO, Supplier<Object> supplier);

    /**
     * 释放锁
     * @param lockKey
     * @return
     */
    boolean  releaseKey(String lockKey);

}
