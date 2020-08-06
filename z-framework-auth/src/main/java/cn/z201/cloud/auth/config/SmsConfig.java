package cn.z201.cloud.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
@ConfigurationProperties(prefix = "sms")
@Data
public class SmsConfig {

    String name;

}
