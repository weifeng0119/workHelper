package server.restServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weifeng
 * @description eureka服务测试
 * @date
 */
@RestController
@RequestMapping("/eureka")
public class EurekaServerControllerTest {
    @Value("${server.port}")
    private String port;
    @RequestMapping("/getPort")
    public String getPort(){
        return "当前端口:"+port;
    }
}
