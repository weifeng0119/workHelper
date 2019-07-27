package com.client.aop;

import com.songshu.snt.autobots.DTO.TmsLockDTO;
import com.songshu.snt.autobots.aop.annotation.TmsSync;
import com.songshu.snt.autobots.constant.SystemConstant;
import com.songshu.snt.autobots.service.TmsSyncService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author weifeng
 * @description redis同步锁实现
 * @date 2019/3/27
 */
@Component
@Aspect
public class TmsSyncAspect {
    private static final Logger logger = LoggerFactory.getLogger(TmsSyncAspect.class);
    @Autowired
    private TmsSyncService syncService;

    @Around("@annotation(com.songshu.snt.autobots.aop.annotation.TmsSync)")
    public Object lock(ProceedingJoinPoint pjp) {
        //配置锁定参数
        TmsSync lockInfo = getLockInfo(pjp);
        if (lockInfo == null) {
            throw new IllegalArgumentException("配置参数错误");
        }
        String synKey = getSynKey(pjp, lockInfo.synKey());
        if (synKey == null || "".equals(synKey)) {
            throw new IllegalArgumentException("配置参数synKey错误");
        }
        TmsLockDTO tmsLockDTO=new TmsLockDTO();
        tmsLockDTO.setLockKey(synKey);
        tmsLockDTO.setKeepMills(lockInfo.keepMills());
        tmsLockDTO.setMaxSleepMills(lockInfo.maxSleepMills());
        //执行锁定
        return syncService.concurrentSafeInvoke(tmsLockDTO,()->{
            Object[] args = pjp.getArgs();
            try {
                return pjp.proceed(args);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                doThrow(throwable);
            }
            return null;
        });
    }
    static <E extends Throwable> void doThrow(Throwable throwable) throws E {
        throw (E)throwable;
    }
    /**
     * 获取包括方法参数上的key
     * redis key的拼写规则为 "SystemConstant.TMS_REDIS_LOCK_KEY+" + synKey + TmsLockDTO id
     */
    private String getSynKey(ProceedingJoinPoint pjp, String synKey) {
        try {
            synKey = SystemConstant.TMS_REDIS_LOCK_KEY + "_" + synKey;
            Object[] args = pjp.getArgs();
            if (args != null && args.length > 0) {
                for (int a = 0; a < args.length; a++) {
                    if (args[a] instanceof TmsLockDTO) {
                        TmsLockDTO tmsLockDTO = (TmsLockDTO) args[a];
                        synKey += "_" + tmsLockDTO.getId();
                    }
                }
                return synKey;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取RedisLock注解信息
     */
    private TmsSync getLockInfo(ProceedingJoinPoint pjp) {
        try {
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Method method = methodSignature.getMethod();
            return method.getAnnotation(TmsSync.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

