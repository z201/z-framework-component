package cn.z201.cloud.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cn.z201.cloud.auth.enums.PlatformUserMessageDetailsEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformUserMessageDetailsDataSourceDto {

    /**
     * 业务活动来源
     */
    String clientBusinessActivitySource;

    /**
     * 微信的openId
     */
    String openId;

    /**
     * 友盟Id
     */
    String umengId;

    /**
     *
     */
    String appId;

    /**
     * 友盟 客户端来源 1 iOS 2 安卓
     */
    Integer umengClientEnvSource;

    /**
     * 类型
     */
    PlatformUserMessageDetailsEnum platformUserMessageDetailsEnum;


}
