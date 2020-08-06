package cn.z201.cloud.registerd.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author z201.coding@gamil.com
 */

@SpringBootApplication
@EnableEurekaServer
@EnableCircuitBreaker
@ComponentScan(basePackages = {"cn.z201.cloud"})
public class ZFrameworkEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZFrameworkEurekaApplication.class, args);
    }
}
