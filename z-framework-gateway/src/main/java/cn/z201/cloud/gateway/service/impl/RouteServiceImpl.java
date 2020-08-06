package cn.z201.cloud.gateway.service.impl;

import cn.z201.cloud.gateway.service.RouteServiceI;
import cn.z201.cloud.gateway.utils.GatewayCacheKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

import static java.util.Collections.synchronizedMap;

@Service
public class RouteServiceImpl implements RouteServiceI {

    private static final Logger log = LoggerFactory.getLogger(RouteServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * ConcurrentHashMap
     */
//    private Map<String, RouteDefinition> routeDefinitionMaps = new ConcurrentHashMap<>();

    /**
     * 这个官方默认是这么写，不知道那种性能更优秀。
     */
    private final Map<String, RouteDefinition> routeDefinitionMaps = synchronizedMap(new LinkedHashMap<>());

    @Override
    public void loadRouteDefinition() {
        Set<String> gatewayKeys = stringRedisTemplate.keys(GatewayCacheKey.GATEWAY_ROUTES + "*");
        if (CollectionUtils.isEmpty(gatewayKeys)) {
            return;
        }
        List<String> gatewayRoutes = Optional.ofNullable(stringRedisTemplate.opsForValue().multiGet(gatewayKeys)).orElse(Lists.newArrayList());
        gatewayRoutes.forEach(value -> {
            try {
                RouteDefinition routeDefinition = new ObjectMapper().readValue(value, RouteDefinition.class);
                routeDefinitionMaps.put(routeDefinition.getId(), routeDefinition);
                log.info(" routeDefinition {}   ", value);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        if (routeDefinitionMaps.isEmpty()) {
            loadRouteDefinition();
        }
        return Flux.fromIterable(routeDefinitionMaps.values());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> routeDefinitionMono) {
        return routeDefinitionMono.flatMap(routeDefinition -> {
            routeDefinitionMaps.put(routeDefinition.getId(), routeDefinition);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            routeDefinitionMaps.remove(id);
            return Mono.empty();
        });
    }
}
