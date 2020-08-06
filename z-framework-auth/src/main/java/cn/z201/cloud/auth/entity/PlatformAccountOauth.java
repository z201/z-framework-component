package cn.z201.cloud.auth.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * platform_account_oauth
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformAccountOauth implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 平台用户全局标识 扩展字段
     */
    private String uid;

    /**
     * vlink_platform_account.id 默认是0，如果直接使用微信登录会要求绑定手机号，若没绑定则默认0
     */
    private Long accountId;

    /**
     * 系统编号
     */
    private String systemCode;

    /**
     * 第三方Oauth名称
     */
    private String oauthName;

    /**
     * 第三方Oauth编号-扩展保留字段
     */
    private Integer oauthCode;

    /**
     * 第三方主体下用户唯一标识 微信是unionid QQ是openId 微博是 uid 
     */
    private String oauthId;

    /**
     * 应用授权open_id
     */
    private String oauthOpenId;

    /**
     * 第三方获取的用户名称 有则存，无默认 EMPTY STRING
     */
    private String oauthUserName;

    /**
     * oauth_access_token 令牌 扩展字段
     */
    private String oauthAccessToken;

    /**
     * oauth_refresh_token 令牌 扩展字段
     */
    private String oauthRefreshToken;

    /**
     * oauth_expires 过期时间 扩展字段
     */
    private Long oauthExpires;

    /**
     * scope 权限 扩展字段
     */
    private String oauthScope;

    /**
     * 数据是否有效 1 有效 0 无效 默认1
     */
    private Integer isEffective;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 第三方注册用户基础信息json
     */
    private String oauthJsonData;

    private static final long serialVersionUID = 1L;
}