package cn.z201.cloud.gateway.filter;

import cn.z201.cloud.core.utils.PlatformHttpHeadersKey;
import cn.z201.cloud.gateway.service.AuthServiceI;
import cn.z201.cloud.gateway.utils.GatewayCacheKey;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cn.z201.cloud.gateway.dto.Result;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 请求url权限校验
 *
 * @author z201.coding@gmail.com
 */
@Configuration
public class AccessGatewayFilter implements GlobalFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessGatewayFilter.class);

    private static final String START_TIME = "startTime";


    public AccessGatewayFilter() {
        log.info("Loaded AccessGatewayFilter [Logging]");
    }

    @Autowired
    AuthServiceI authService;

    /**
     * 1.首先网关检查token是否有效，无效直接返回401，不调用签权服务
     * 2.调用签权服务器看是否对该请求有权限，有权限进入下一个filter，没有权限返回401
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        /**
         * 授权信息
         */
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String clientBusinessGroupSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_BUSINESS_GROUP_SOURCE);
        String clientBusinessSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_BUSINESS_SOURCE);
        String clientBusinessActivitySource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_BUSINESS_ACTIVITY_SOURCE);
        String clientEnvSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_EVN_SOURCE);
        String clientPlatformSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_PLATFORM_SOURCE);
        String clientStartTime = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_START_TIME);
        String clientVersionSource = request.getHeaders().getFirst(PlatformHttpHeadersKey.CLIENT_VERSION_SOURCE);
        String clientIp = request.getHeaders().getFirst(PlatformHttpHeadersKey.X_REAL_IP);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        if (Strings.isEmpty(clientBusinessGroupSource) ||
                Strings.isEmpty(clientBusinessSource) ||
                Strings.isEmpty(clientEnvSource) ||
                Strings.isEmpty(clientVersionSource)) {
//            return buildResponse(exchange,Result.fail("请求头不合法"));
        }
        String url = request.getPath().value();
        String method = exchange.getRequest().getMethod().name();
        String host = exchange.getRequest().getURI().getHost();
        String path = exchange.getRequest().getURI().getPath();
        String info = String.format("Host:{%s} Ip:{%s} Group:{%s} Source:{%s} Env:{%s} Version:{%s} Method:{%s} Path:{%s}",
                host,
                clientIp,
                clientBusinessGroupSource,
                clientBusinessSource,
                clientEnvSource,
                clientVersionSource,
                method,
                path

        );


        //不需要网关签权的url
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        //不需要网关签权的url
        if (authService.ignoreAuthentication(url)) {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Long startTime = exchange.getAttribute(START_TIME);
                if (startTime != null) {
                    Long executeTime = (System.currentTimeMillis() - startTime);
                    log.info(" {}  ignore : {}", info, executeTime + "ms");
                }
            }));
        }
        // 如果请求未携带token信息, 直接跳出
        if (StringUtils.isBlank(authentication) ||
                !authentication.startsWith(GatewayCacheKey.BEARER)) {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                Long startTime = exchange.getAttribute(START_TIME);
                if (startTime != null) {
                    Long executeTime = (System.currentTimeMillis() - startTime);
                    log.info(" {} Auth-false {}", info, executeTime + "ms");
                }
            }));
//            return unauthorized(exchange);
        }
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                Long executeTime = (System.currentTimeMillis() - startTime);
                log.info(" {} Auth-true {}", info, executeTime + "ms");
            }
        }));
    }

    /**
     * 网关直接响应
     *
     * @param
     */
    private Mono<Void> buildResponse(ServerWebExchange serverWebExchange, Result result) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
        Gson gson = new Gson();
        DataBuffer buffer = serverWebExchange.getResponse()
                .bufferFactory().wrap(gson.toJson(result).getBytes());
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }
}
