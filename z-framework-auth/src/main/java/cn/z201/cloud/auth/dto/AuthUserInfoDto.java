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
public class AuthUserInfoDto {

    /**
     * 账号id
     */
    private Long accountId;

    /**
     * 扩展字段
     */
    private String openIp;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户网址
     */
    private String blog;
    /**
     * 所在公司
     */
    private String company;
    /**
     * 位置
     */
    private String location;

    /**
     * 用户备注（各平台中的用户个人介绍）
     */
    private String remark;

    /**
     * 性别
     */
    private String gender;

    /**
     * 用户来源
     */
    private String source;

    /**
     * 注册类型 1、手机号注册 2、邮箱注册 3、第三方注册
     */
    private Integer sourceType;

}
