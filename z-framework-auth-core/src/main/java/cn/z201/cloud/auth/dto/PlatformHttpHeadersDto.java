package cn.z201.cloud.auth.dto;

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
public class PlatformHttpHeadersDto {

    /**
     * 业务组来源
     */
    String clientBusinessGroupSource;
    /**
     * 业务客户端来源
     */
    String clientBusinessSource;
    /**
     * 业务活动来源
     */
    String clientBusinessActivitySource;
    /**
     * 客户端环境  安卓 ios windows
     */
    String clientEnvSource;
    /**
     * 客户端来源 xxx手机 xxx 浏览器
     */
    String clientPlatformSource;
    /**
     * 客户端请求时间戳
     */
    String clientStartTime;
    /**
     * 客户端版本号
     */
    String clientVersionSource;
    /**
     * 客户端id
     */
    String clientIp;

}
