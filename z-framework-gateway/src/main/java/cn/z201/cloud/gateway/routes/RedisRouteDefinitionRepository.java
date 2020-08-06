package cn.z201.cloud.gateway.routes;

import cn.z201.cloud.gateway.service.impl.RouteServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RedisRouteDefinitionRepository implements RouteDefinitionRepository {

    @Autowired
    private RouteServiceImpl routeServiceImpl;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return routeServiceImpl.getRouteDefinitions();
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return routeServiceImpl.save(route);
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeServiceImpl.delete(routeId);
    }

}
