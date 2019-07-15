package com.client.config;

import feign.Feign;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author weifeng
 * @description okclient配置
 * @date 2019/7/13
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkClientConfig {
    @Bean
    public okhttp3.OkHttpClient okHttpClient(){
        return new okhttp3.OkHttpClient.Builder().
                //连接超时
                connectTimeout(60, TimeUnit.SECONDS).
                //读超时
                readTimeout(60,TimeUnit.SECONDS).
                //写超时
                writeTimeout(60,TimeUnit.SECONDS).
                //失败重试
                retryOnConnectionFailure(true).connectionPool(new ConnectionPool()).build();
    }
}
