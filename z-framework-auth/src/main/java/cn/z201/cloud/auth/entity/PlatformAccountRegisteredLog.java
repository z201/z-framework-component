package cn.z201.cloud.auth.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * platform_account_registered_log
 * @author 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlatformAccountRegisteredLog implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 登录凭证主键
     */
    private Long accountId;

    /**
     * 注册ip来源
     */
    private String registeredIp;

    /**
     * 注册客户端类型 0 未知 1 安卓 2 ios 3 web
     */
    private Integer registeredClient;

    /**
     * 注册来源  0 未知
     */
    private Integer registeredSource;

    /**
     * 注册渠道 安卓使用
     */
    private String channel;

    /**
     * 注册时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    private static final long serialVersionUID = 1L;
}