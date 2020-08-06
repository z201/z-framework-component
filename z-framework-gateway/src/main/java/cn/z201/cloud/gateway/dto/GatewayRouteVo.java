package cn.z201.cloud.gateway.dto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.z201.cloud.gateway.dto.entity.GatewayRoute;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
@Data
public class GatewayRouteVo {

    private static final Logger log = LoggerFactory.getLogger(GatewayRouteVo.class);

    /**
     * 路由的id
     */
    private String id;
    /**
     * 路由规则转发的目标uri
     */
    private String uri;
    /**
     * 路由执行的顺序
     */
    private Integer order;
    /**
     * 路由断言集合配置
     */
    private List<FilterDefinition> filters = new ArrayList<>();
    /**
     * 路由过滤器集合配置
     */
    private List<PredicateDefinition> predicates = new ArrayList<>();

    public GatewayRouteVo(GatewayRoute gatewayRoute) {
        this.id = gatewayRoute.getId();
        this.uri = gatewayRoute.getUri();
        this.order = gatewayRoute.getOrders();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.filters = objectMapper.readValue(gatewayRoute.getFilters(), new TypeReference<List<FilterDefinition>>() {
            });
            this.predicates = objectMapper.readValue(gatewayRoute.getPredicates(), new TypeReference<List<PredicateDefinition>>() {
            });
        } catch (IOException e) {
            log.error("网关路由对象转换失败", e);
        }
    }
}
