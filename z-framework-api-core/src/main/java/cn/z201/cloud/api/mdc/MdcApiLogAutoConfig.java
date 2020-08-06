package cn.z201.cloud.api.mdc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author z201.coding@gamil.com
 * 日志拦截器，排除对spring cloud gateway的影响 (WebMvcConfigurer)
 */
@ConditionalOnClass(WebMvcConfigurer.class)
@Slf4j
public class MdcApiLogAutoConfig {

    public MdcApiLogAutoConfig() {
        log.info("Loaded Vlink-MDC-Log [V1.0.0]");
    }

    @Bean
    public FilterRegistrationBean requestContextRepositoryFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MdcTraceContextFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
