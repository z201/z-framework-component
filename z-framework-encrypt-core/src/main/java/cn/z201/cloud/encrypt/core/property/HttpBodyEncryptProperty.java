package cn.z201.cloud.encrypt.core.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@ConfigurationProperties(prefix = "encrypt")
@Data
public class HttpBodyEncryptProperty {
    /**
     * 是否开启
     */
    private Boolean enable;

}
