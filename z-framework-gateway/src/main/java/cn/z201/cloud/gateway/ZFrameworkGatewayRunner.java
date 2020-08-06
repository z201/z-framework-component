package cn.z201.cloud.gateway;

import cn.z201.cloud.gateway.service.impl.RouteServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
public class ZFrameworkGatewayRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(ZFrameworkGatewayRunner.class);

    @Autowired
    private RouteServiceImpl routeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("init Z gateway ");
//        routeService.loadRouteDefinition();
    }
}
