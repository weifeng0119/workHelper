package com.client.eurekaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

/**
 * 继承Application接口后项目启动时会按照执行顺序执行run方法
 * 通过设置Order的value来指定执行的顺序
 */
@Component
@Order(value = 1)
public class StartService implements ApplicationRunner {
    @Autowired
    private TestFeign testFeign;
    @Override
    public void run(ApplicationArguments args) throws Exception {
            System.out.println("远程调用服务,端口:"+testFeign.getPort());
    }
}