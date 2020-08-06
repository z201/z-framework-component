package cn.z201.cloud.auth.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * platform_system_oauth
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformSystemOauth implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * platform_system_id
     */
    private String systemCode;

    /**
     * appId
     */
    private String key;

    /**
     * AppSecret
     */
    private String secretId;

    /**
     * 0表示未知,去代码里面看Oauth2枚举 
     */
    private Integer oauthType;

    /**
     * 是否启用 1 启动 0 不启用
     */
    private Integer enable;

    /**
     * 维护信息
     */
    private String maintenanceInfo;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    private static final long serialVersionUID = 1L;
}