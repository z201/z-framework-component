package cn.z201.cloud.gateway.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.ref.PhantomReference;
import java.util.List;


/**
 * @author z201.coding@gmail.com
 **/
@ConfigurationProperties(prefix = "vlink.gateway")
@Data
public class VlinkGatewayProperty {

    /**
     * oauth jwt 验签key
     */
    private String oauth2JwtSignKey;

    /**
     * 白名单
     */
    private String ignoreUrlStartWith;


}
