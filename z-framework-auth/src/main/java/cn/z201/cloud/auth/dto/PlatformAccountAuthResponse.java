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
public class PlatformAccountAuthResponse {

    /**
     * 账号主键
     */
    Long accountId;

    /**
     * uid
     */
    String uid;

    /**
     * 授权响应码
     */
    String code;

    /**
     * 授权系统
     */
    String systemCode;

    /**
     * 时间戳
     */
    Long timestamp;

}
