package cn.z201.cloud.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformAccountDto {


    /**
     * 用户邮箱
     */
    String email;

    Long accountId;

    /**
     * 系统appId
     */
    String appId;

    /**
     * 系统编号
     */
    String systemCode;

    /**
     * 授权类型
     */
    Integer oauthType;

    /**
     * 手机号
     */
    String phone;

    /**
     * 新的手机号
     */
    String newPhone;

    /**
     * 短信验证码
     */
    String smsCode;

    /**
     * 新手机号 短信验证码
     */
    String newPhoneSmsCode;

    /**
     * 密码
     */
    String pwd;

    /**
     * 密码2
     */
    String pwd2;

    /**
     * 新密码1
     */
    String newPwd;

    /**
     * 新密码2
     */
    String newPwd2;

    /**
     * 通用字段 类型
     */
    Integer type;

    /**
     * oauth code码 微信
     */
    String code;

    /**
     * oauth access_token qq weibo
     */
    String accessToken;

    /**
     * oauth weibo
     */
    String uId;

    /**
     * oauth qq
     */
    String openId;

    /**
     * 安卓渠道
     */
    String channel;

    /**
     * 冗余字段 手机号 或者 email
     */
    String account;

    /**
     * 图片id
     */
    String imageId;

    // https://developers.weixin.qq.com/miniprogram/dev/api/open-api/user-info/wx.getUserInfo.html
    /**
     * 微信小程序更新用户信息加密体
     */
    String encryptedData;


    String iv;

    List<Long> accountIds;

    String ip;
}
