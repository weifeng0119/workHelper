package com.client.service.sync.impl;

import com.songshu.snt.autobots.DTO.TmsLockDTO;
import com.songshu.snt.autobots.constant.TmsErrEnum;
import com.songshu.snt.autobots.exception.TmsException;
import com.songshu.snt.autobots.service.TmsSyncService;
import com.songshu.snt.base.common.SntResult;
import com.songshu.snt.base.common.exception.SntSedisException;
import com.songshu.snt.base.sedis.SedisManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * @author weifeng
 * @description Tms同步并发服务
 * @date 2019/3/28
 */
@Service
public class TmsSyncServiceImpl implements TmsSyncService {
    private static Logger logger = LoggerFactory.getLogger(TmsSyncServiceImpl.class);
    @Autowired
    private SedisManage sedisManage;

    /**
     * redis加锁,必须设置超时时间,否则报错
     *
     * @param supplier
     * @return
     */
    @Override
    public boolean getLockByRedis(Supplier<TmsLockDTO> supplier) {
        TmsLockDTO tmsLockDTO = supplier.get();
        try {
            if (tmsLockDTO == null || tmsLockDTO.getKeepMills() == null) {
                throw new TmsException(TmsErrEnum.KEEP_LOCK_TIME);
            }
            invokeRedisLock(tmsLockDTO);
            return true;
        } catch (SntSedisException e) {
            //锁失败捕获异常
            e.printStackTrace();
            logger.info("{}锁定失败详情:{}", tmsLockDTO.getLockKey(), e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("{}锁定失败详情:{}", tmsLockDTO.getLockKey(), e.getMessage());
            throw e;
        }
    }

    /**
     * 并发执行操作
     *
     * @param tmsLockDTO
     * @param supplier
     * @return
     */
    @Override
    public Object concurrentSafeInvoke(TmsLockDTO tmsLockDTO, Supplier<Object> supplier) {
        String releaseKey = "";
        try {
            if (tmsLockDTO == null || tmsLockDTO.getKeepMills() == null) {
                throw new TmsException(TmsErrEnum.KEEP_LOCK_TIME);
            }
            // 得到锁
            releaseKey = invokeRedisLock(tmsLockDTO);
            return supplier.get();
        } catch (SntSedisException e) {
            //锁失败捕获异常
            e.printStackTrace();
            logger.info("lockkey为:{}锁定失败详情:{}", tmsLockDTO.getLockKey(), e.getMessage());
            return SntResult.fail(TmsErrEnum.SYNC_LOCK.getCode(), TmsErrEnum.SYNC_LOCK.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("lockkey为:{}并发操作失败,详情:{}", tmsLockDTO.getLockKey(), e.getMessage());
            throw e;
        } finally {
            if (!StringUtils.isEmpty(releaseKey)) {
                sedisManage.releaseLock(tmsLockDTO.getLockKey(), releaseKey);
                logger.info("lockkey为:{}锁释放成功", tmsLockDTO.getLockKey());
            }
        }
    }

    @Override
    public boolean releaseKey(String lockKey) {
        return sedisManage.remove(lockKey);
    }

    private String invokeRedisLock(TmsLockDTO tmsLockDTO) {
        // 得到锁
        String releaseKey = sedisManage.tryLock(tmsLockDTO.getLockKey(), tmsLockDTO.getMaxSleepMills(), tmsLockDTO.getKeepMills());
        logger.info(tmsLockDTO.getLockKey() + "锁定成功");
        return releaseKey;
    }


}
