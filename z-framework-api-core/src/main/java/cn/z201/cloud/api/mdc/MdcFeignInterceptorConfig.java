package cn.z201.cloud.api.mdc;

import cn.z201.cloud.api.utils.HttpApiConstant;
import cn.z201.cloud.api.utils.HttpApiContextHandler;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


/**
 * @author z201.coding@gmail.com
 * 自定义restTemplate拦截器
 * 这里可以把一些参数从应用层传到内部服务
 **/
@Slf4j
@ConditionalOnClass(WebMvcConfigurer.class)
public class MdcFeignInterceptorConfig implements RequestInterceptor, HttpApiConstant {

    public MdcFeignInterceptorConfig() {
        log.info("Loaded Vlink-REST-INTERCEPTOR [V1.0.0]");
    }

    @Override
    public void apply(RequestTemplate template) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest();
            String xRealIp = request.getHeader(X_REAL_IP);
            String authentication = request.getHeader(HTTP_TOKEN_HEADER);
            String appTraceId = request.getHeader(HTTP_HEADER_TRACE_ID);
            String businessGroupSource = request.getHeader(CLIENT_BUSINESS_GROUP_SOURCE);
            String clientBusinessSource = request.getHeader(CLIENT_BUSINESS_SOURCE);
            String clientBusinessActivitySource = request.getHeader(CLIENT_BUSINESS_ACTIVITY_SOURCE);
            String clientEnvSource = request.getHeader(CLIENT_EVN_SOURCE);
            String clientPlatformSource = request.getHeader(CLIENT_PLATFORM_SOURCE);
            String clientStartTime = request.getHeader(CLIENT_START_TIME);
            String clientVersionSource = request.getHeader(CLIENT_VERSION_SOURCE);
            if (Strings.isEmpty(appTraceId)) {
                appTraceId = MDC.get(HttpApiConstant.HTTP_HEADER_TRACE_ID);
                if (Strings.isEmpty(appTraceId)) {
                    appTraceId = HttpApiContextHandler.currentTraceId();
                }
            }
            template.header(HttpHeaders.ACCEPT_ENCODING, "gzip");
            template.header(X_REAL_IP, xRealIp);
            template.header(HTTP_TOKEN_HEADER, authentication);
            template.header(HTTP_HEADER_TRACE_ID, appTraceId);
            template.header(CLIENT_BUSINESS_GROUP_SOURCE, businessGroupSource);
            template.header(CLIENT_BUSINESS_SOURCE, clientBusinessSource);
            template.header(CLIENT_BUSINESS_ACTIVITY_SOURCE, clientBusinessActivitySource);
            template.header(CLIENT_EVN_SOURCE, clientEnvSource);
            template.header(CLIENT_PLATFORM_SOURCE, clientPlatformSource);
            template.header(CLIENT_START_TIME, clientStartTime);
            template.header(CLIENT_VERSION_SOURCE, clientVersionSource);
            if (log.isDebugEnabled()) {
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String value = request.getHeader(name);
                    log.debug("header {} - {}", name, value);
                }
            }
        } catch (Exception e) {
            log.error("template exception {}",e.getMessage());
        }
    }
}
