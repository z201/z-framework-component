package cn.z201.cloud.auth.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * platform_account_update_log
 * @author 
 */
@Data
public class PlatformAccountUpdateLog implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 账号主键
     */
    private Long accountId;

    /**
     * 标题
     */
    private String title;

    /**
     * 更新日志描述
     */
    private String content;

    /**
     * 类型 0 其他 1 绑定手机号  2 换绑手机号 3 绑定其他 4 解绑其他
     */
    private Integer type;

    /**
     * 更新时间
     */
    private Long createTime;

    /**
     * 创建时间
     */
    private Long updateTime;

    private static final long serialVersionUID = 1L;
}