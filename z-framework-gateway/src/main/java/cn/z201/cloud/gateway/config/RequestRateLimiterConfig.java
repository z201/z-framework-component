package cn.z201.cloud.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 自定义限流标志的key，多个维度可以从这里入手
 * exchange对象中获取服务ID、请求信息，用户信息等
 */
@Component
public class RequestRateLimiterConfig {

    /**
     * ip地址限流
     *
     * @return 限流key
     */
    @Bean
    @Primary
    public KeyResolver remoteAddressKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    /**
     * 请求路径限流
     *
     * @return 限流key
     */
    @Bean
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    /**
     * userId 限流
     *
     * @return 限流key
     */
//    @Bean
//    public KeyResolver userKeyResolver() {
        // TODO 这里从请求里面获取名字
//        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("username"));
//    }
}
