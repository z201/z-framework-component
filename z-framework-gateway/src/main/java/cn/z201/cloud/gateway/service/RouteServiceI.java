package cn.z201.cloud.gateway.service;

import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteServiceI {

    /**
     * 装载动态路由
     */
    void loadRouteDefinition();

    /**
     * 获取路由信息
     * @return
     */
    Flux<RouteDefinition> getRouteDefinitions();

    /**
     * 新增路由信息
     * @param routeDefinitionMono
     * @return
     */
    Mono<Void> save(Mono<RouteDefinition> routeDefinitionMono);

    /**
     * 删除路由信息
     * @param routeId
     * @return
     */
    Mono<Void> delete(Mono<String> routeId);
}
