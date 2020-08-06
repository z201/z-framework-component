package cn.z201.cloud.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cn.z201.cloud.auth.enums.PlatformUserMessageDetailsEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlatformUserMessageDataSourceDto {

    /**
     * 平台账号id
     */
    Long accountId;

    /**
     * 业务系统userId
     */
    Long userId;

    /**
     * 业务活动来源
     */
    String clientBusinessActivitySource;


    /**
     * 么有就是null
     * key 客户端枚举
     * value 消息相关信息
     */
    Map<PlatformUserMessageDetailsEnum, PlatformUserMessageDetailsDataSourceDto> platformUserMessageDetailsDataSourceMap;

    public PlatformUserMessageDetailsDataSourceDto findByPlatformUserMessageDetailsEnum(PlatformUserMessageDetailsEnum platformUserMessageDetailsEnum) {
        Iterator<Map.Entry<PlatformUserMessageDetailsEnum, PlatformUserMessageDetailsDataSourceDto>> it =
                platformUserMessageDetailsDataSourceMap.entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry<PlatformUserMessageDetailsEnum, PlatformUserMessageDetailsDataSourceDto> entry = it.next();
            if (entry.getKey().getValue() == platformUserMessageDetailsEnum.getValue()) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void addPlatformUserMessageDetailsDataSourceDto(PlatformUserMessageDetailsDataSourceDto platformUserMessageDetailsDataSourceDto) {
        if (null == platformUserMessageDetailsDataSourceMap || platformUserMessageDetailsDataSourceMap.isEmpty()) {
            platformUserMessageDetailsDataSourceMap = new HashMap<>();
        }
        this.platformUserMessageDetailsDataSourceMap.put(platformUserMessageDetailsDataSourceDto.getPlatformUserMessageDetailsEnum(), platformUserMessageDetailsDataSourceDto);
    }

}
