package cn.z201.cloud.gateway;

import cn.z201.cloud.gateway.property.VlinkGatewayProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author z201.coding@gamil.com
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"cn.z201.cloud"})
@EnableConfigurationProperties({VlinkGatewayProperty.class})
public class ZFrameworkGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZFrameworkGatewayApplication.class, args);
    }
}
