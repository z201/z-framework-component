package cn.z201.cloud.auth.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * platform_account
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformAccount implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 系统全局唯一标识
     */
    private String uid;

    /**
     * 手机号(当前用户身份唯一标识)
     */
    private String phoneNumber;

    /**
     * 邮箱 保留字段，考虑邮箱登录。
     */
    private String email;

    /**
     * 登录凭证密码
     */
    private String password;

    /**
     * 加盐后密码 扩展字段当前密码加密过弱
     */
    private String saltPassword;

    /**
     * 盐 扩展字段
     */
    private String salt;

    /**
     * 数据是否有效 1 有效 0 无效
     */
    private Integer isEffective;

    /**
     * 兼容老的user_id 0表示数据未同步
     */
    private Long oldUid;

    /**
     * 业务来源主键
     */
    private Long systemId;

    /**
     * 业务系统编号
     */
    private String systemCode;

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