package com.client.eurekaService;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Feign服务调用测试 name为服务应用名
 */
@FeignClient(value="eurekaServer")
public interface TestFeign {
    @RequestMapping(value = "/eureka/getPort",method = RequestMethod.GET)
    String getPort();
}