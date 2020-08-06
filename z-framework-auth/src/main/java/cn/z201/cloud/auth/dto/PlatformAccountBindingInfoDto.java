package cn.z201.cloud.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformAccountBindingInfoDto {

    /**
     * 0 phone 1 weChart 2 weibo 3 qq
     */
    Integer code;

    /**
     * 绑定相关名称
     */
    String name;

    /**
     * 绑定机制是否可用 true 可用  false 不可用
     */
    Boolean status;

    /**
     * 账号是否绑定 true 绑定 false 未绑定
     */
    Boolean bindingStatus;

    /**
     * 账号绑定相关 信息 默认填写名称（老数据未填写）
     */
    String bindingInfo;
}
