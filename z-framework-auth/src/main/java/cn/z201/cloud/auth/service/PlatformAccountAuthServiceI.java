package cn.z201.cloud.auth.service;

import cn.z201.cloud.auth.dto.PlatformAccountDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountAuthServiceI {

    /**
     * 凭证绑定信息
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountBindingInfo(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 第三方oauth授权
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 小程序oauth授权
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountMiniProgramOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * wap端账号授权
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountWapOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 小程序oauth授权注册
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountMiniProgramRegistered(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 小程序oauth获取手机号信息并绑定
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountMiniProgramPhoneBinding(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 小程序oauth准备获取手机号信息并绑定
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountMiniProgramPhoneReadyPhoneBinding(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 绑定第三方oauth
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountBindingOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 解绑第三方oauth
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object accountUntiedOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 发送短信
     *
     * @param platformAccountDto
     * @param request
     * @return
     */
    Object phoneSendSms(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 手机号检查短信
     *
     * @param platformAccountDto
     * @param request
     * @return
     */
    Object phoneCheckSms(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 手机短信登录
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object phoneSmsLogin(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 手机号密码登录
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object phonePasswordLogin(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 短信找回密码
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object smsCodePasswordForget(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 设置 密码
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object changePassword(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 更换手机号
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object changeBindingPhone(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 绑定手机号
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object bindingPhone(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 检查手机号状态
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    Object checkPhone(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 获取图形验证码
     *
     * @param response
     */
    void verifyImage(HttpServletRequest request, HttpServletResponse response);

    /**
     * 判断图片验证码是否正确
     *
     * @param platformAccountDto
     */
    Object verifyImageStatus(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 图形验证码登录
     *
     * @param platformAccountDto
     * @return
     */
    Object verifyImageCodeLogin(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response);


}
