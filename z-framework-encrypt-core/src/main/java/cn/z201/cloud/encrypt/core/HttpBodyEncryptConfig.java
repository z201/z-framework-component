package cn.z201.cloud.encrypt.core;

import cn.z201.cloud.encrypt.core.advice.DecryptRequestBodyAdvice;
import cn.z201.cloud.encrypt.core.property.HttpBodyEncryptProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
@EnableConfigurationProperties({HttpBodyEncryptProperty.class})
@ConditionalOnProperty(name = "encrypt.enable", havingValue = "true", matchIfMissing = true)
@Import({DecryptRequestBodyAdvice.class,
        HttpConverterConfig.class})
public class HttpBodyEncryptConfig {

    private static final Logger log = LoggerFactory.getLogger(HttpBodyEncryptConfig.class);

    public HttpBodyEncryptConfig() {
        log.info("Loaded Z-MDC-Log [V1.0.0]");
    }

}
