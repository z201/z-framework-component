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
public class AuthTokenDto {

    /**
     * 小程序注册到时候用到
     */
    private String sessionKey;

    /**
     * 授权token
     */
    private String accessToken;

    /**
     * 过期时间
     */
    private int expireIn;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 用户标识
     */
    private String uid;
    /**
     * 用户标识
     */
    private String openId;
    /**
     * 用户授权码
     */
    private String accessCode;
    /**
     * 用户标识
     */
    private String unionId;

    /**
     * 用户信息
     */
    private AuthUserInfoDto userInfoDto;


}
