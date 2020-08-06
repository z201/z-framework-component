package cn.z201.cloud.auth.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * platform_account_device_umeng
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformAccountDeviceUmeng implements Serializable {
    private Long id;

    /**
     * 平台编号
     */
    private String systemCode;

    /**
     * 友盟 设备id
     */
    private String deviceId;

    private Integer clientEnvSource;

    private Long platformAccountId;

    private Boolean isDel;

    private Long updateTime;

    private Long createTime;

    private static final long serialVersionUID = 1L;
}