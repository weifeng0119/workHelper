package com.client.restClient;

import com.client.eurekaService.TestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author weifeng
 * @description eureka服务测试
 * @date
 */
@RestController
@RequestMapping("/eurekaClient")
public class EurekaClientControllerTest {
    @Autowired
    private TestFeign testFeign;
    @GetMapping("/getPort")
    public String getPort(){
        return testFeign.getPort();
    }
}
