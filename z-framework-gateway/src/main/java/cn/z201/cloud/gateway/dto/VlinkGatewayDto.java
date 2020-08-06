package cn.z201.cloud.gateway.dto;

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
public class VlinkGatewayDto {

    String opId;
    String businessGroupSource;
    String clientBusinessSource;
    String clientBusinessActivitySource;
    String clientEnvSource;
    String clientPlatformSource;
    String clientStartTime;
    String clientVersionSource;
}
