package cn.z201.cloud.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author z201.coding@gamil.com
 */
@SpringBootApplication
@EnableDiscoveryClient
//@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"cn.z201.cloud"})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan("cn.z201.cloud")
@MapperScan("com.vlink.cloud.*.dao")
@EnableAsync
public class ZFrameworkAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZFrameworkAuthApplication.class, args);
    }

}
