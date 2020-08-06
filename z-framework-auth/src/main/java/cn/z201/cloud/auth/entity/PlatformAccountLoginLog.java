package cn.z201.cloud.auth.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * platform_account_login_log
 * @author 
 */
@Data
public class PlatformAccountLoginLog implements Serializable {
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
    private String loginIp;

    /**
     * 登录客户端类型 0 未知 1 安卓 2 ios 3 web
     */
    private Integer loginClient;

    /**
     * 登录来源  0 未知
     */
    private Integer loginSource;

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