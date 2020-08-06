package cn.z201.cloud.auth.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.Node;

/**
 * platform_account_device_idfa
 * @author 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformAccountDeviceIdfa implements Serializable {
    private Long id;

    /**
     * 系统编号
     */
    private String systemCode;

    private String idfa;

    /**
     * 平台账号
     */
    private Long platformAccountId;

    private Boolean isDel;

    private Long updateTime;

    private Long createTime;

    private static final long serialVersionUID = 1L;
}