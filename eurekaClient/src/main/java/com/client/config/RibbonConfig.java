package com.client.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.WeightedResponseTimeRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RibbonConfig
 * @Description ribbon配置
 * @Author weifeng
 * @Time 2019/7/15
 * @Version 1.0.0
 **/
@Configuration
public class RibbonConfig {
    /**
     * 负载策略配置，选择响应时间权重策略
     * @return
     */
    @Bean
    public IRule feignRule() {
        return new WeightedResponseTimeRule();
    }

}
