package cn.z201.cloud.auth.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

/**
 * platform_system
 * @author 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformSystem implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 系统名称
     */
    private String title;

    /**
     * 业务分组 红豆角HDJ 分闪FS
     */
    private String businessGroup;

    /**
     * 系统编号
     */
    private String code;

    /**
     * 使用账号库
     */
    private String accountLibraryCode;

    /**
     * 系统类型（100  app 、200 web 、 300 微信小程序、 400 快应用 、500 微信公众号)
     */
    private Integer type;

    /**
     * 是否校验请求应用的域名是否合法   0 不检查 1 检查 默认 0  扩展字段
     */
    private String domain;

    /**
     * 单点登录成功后重定向页面到应用的地址  扩展字段
     */
    private String ssoRedirectUri;

    /**
     * 备注信息
     */
    private String desc;

    /**
     * 数据是否有效 1 有效 0 无效
     */
    private Integer isEffective;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    private static final long serialVersionUID = 1L;
}