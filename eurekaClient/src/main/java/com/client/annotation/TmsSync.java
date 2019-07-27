package com.client.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 同步注解
 */
public @interface TmsSync {
    /**
     * 锁的key,此参数必填
     * redis key的拼写规则为 "RedisSyn+" + synKey + businessKey
     */
    String synKey();

    /**
     * 持锁时间，超时时间，持锁超过此时间自动丢弃锁
     * 单位毫秒,默认五分钟
     * 如果为0表示永远不释放锁，在设置为0的情况下toWait为true是没有意义的
     * 但是没有比较强的业务要求下，不建议设置为0
     */
    long keepMills() default 5 * 60 * 1000;

    /**
     * 当获取锁失败，是继续等待还是放弃
     * 默认为放弃
     */
    boolean toWait() default false;

    /**
     * 没有获取到锁的情况下且toWait为继续等待，睡眠指定毫秒数继续获取锁，也就是轮训获取锁的时间
     * 默认为10毫秒
     * @return
     */
    long sleepMills() default 10;

    /**
     * 锁获取超时时间：<br/>
     * 没有获取到锁的情况下且toWait()为true继续等待，最大等待时间，如果超时抛出
     * {java.util.concurrent.TimeoutException.TimeoutException}
     * ，可捕获此异常做相应业务处理；<br/>
     * 单位毫秒,默认一分钟，如果设置为0即为没有超时时间，一直获取下去；
     * @return
     */
    long maxSleepMills() default 60 * 1000;
}
