package cn.z201.cloud.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpApiDto {

    String appTraceId;

    String authorization;

    String tenant;
}
