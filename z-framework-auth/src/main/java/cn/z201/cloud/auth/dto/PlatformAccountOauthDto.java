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
public class PlatformAccountOauthDto {

    /**
     * 应用授权open_id
     */
    String oauthOpenId;

    /**
     * 对应的appId
     */
    String appId;

}
