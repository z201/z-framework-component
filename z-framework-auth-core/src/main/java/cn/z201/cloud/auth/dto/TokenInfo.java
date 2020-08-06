package cn.z201.cloud.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author z201.coding@gmail.com
 **/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfo {

    /**
     * 平台账号
     */
    Long accountId;

    /**
     * 系统用户信息
     */
    Long userId;

    /**
     * 登录系统
     */
    String loginSystemCode;

    /**
     * 用户允许访问系统
     */
    Set<String> systemCodeSet;

    /**
     * 启用唯一在线
     */
    Boolean enableOnlyOnline;

    /**
     * 用户续期时长（时间戳）
     */
    Long renewalTimestamp;

    /**
     * 校验时间间隔（时间戳）
     */
    Long checkInterval;

    /**
     * jwt 过期时间
     */
    Long expiration;
}
