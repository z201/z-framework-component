package cn.z201.cloud.gateway.service.impl;

import cn.z201.cloud.gateway.dto.GatewayRouteVo;
import cn.z201.cloud.gateway.dto.entity.GatewayRoute;
import cn.z201.cloud.gateway.service.GatewayRouteServiceI;
import cn.z201.cloud.gateway.utils.GatewayCacheKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GatewayRouteServiceImpl implements GatewayRouteServiceI {

    private static final Logger log = LoggerFactory.getLogger(GatewayRouteServiceImpl.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RouteServiceImpl routeService;

    @Override
    public long add(GatewayRoute gatewayRoute) {
        // TODO 临时代码这个单独生成传进来吧
        Long gatewayId = 1L;
        stringRedisTemplate.opsForValue().set(GatewayCacheKey.GATEWAY_ROUTES + gatewayId, toJson(new GatewayRouteVo(gatewayRoute)));
        return gatewayId;
    }

    @Override
    public void delete(String id) {
        stringRedisTemplate.delete(GatewayCacheKey.GATEWAY_ROUTES + id);
    }

    @Override
    public void update(GatewayRoute gatewayRoute) {
        stringRedisTemplate.delete(GatewayCacheKey.GATEWAY_ROUTES + gatewayRoute.getId());
        stringRedisTemplate.opsForValue().set(GatewayCacheKey.GATEWAY_ROUTES, toJson(new GatewayRouteVo(get(gatewayRoute.getId()))));
    }

    @Override
    public GatewayRoute get(String id) {
        String jsonStr = stringRedisTemplate.opsForValue().get(GatewayCacheKey.GATEWAY_ROUTES + id);
        GatewayRoute gatewayRoute = null;
        try {
            gatewayRoute = new ObjectMapper().readValue(jsonStr, GatewayRoute.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gatewayRoute;
    }

    @Override
    public boolean overload() {
        routeService.loadRouteDefinition();
        return true;
    }

    /**
     * GatewayRoute转换为json
     * @param gatewayRouteVo redis需要的vo
     * @return json string
     */
    private String toJson(GatewayRouteVo gatewayRouteVo) {
        String routeDefinitionJson = Strings.EMPTY;
        try {
            routeDefinitionJson = new ObjectMapper().writeValueAsString(gatewayRouteVo);
        } catch (JsonProcessingException e) {
            log.error("网关对象序列化为json String", e);
        }
        return routeDefinitionJson;
    }
}
