package cn.z201.cloud.auth.controller;

import cn.z201.cloud.auth.dto.PlatformAccountDto;
import cn.z201.cloud.auth.service.PlatformAccountAuthServiceI;
import cn.z201.cloud.encrypt.core.annotation.DecryptBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
@RequestMapping(PlatformAccountCommonController.ROUTER_INDEX)
public class PlatformAccountCommonController {

    static final String ROUTER_INDEX = "/authorization";

    @Autowired
    PlatformAccountAuthServiceI platformAccountAuthService;

    /**
     * 发送短信
     *
     * @param platformAccountDto
     * @param request
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/sms/send")
    public Object sendSms(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.phoneSendSms(platformAccountDto, request, response);
    }

    /**
     * 检查短信
     *
     * @param platformAccountDto
     * @param request
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/sms/check")
    public Object checkSms(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.phoneCheckSms(platformAccountDto, request, response);
    }

    /**
     * 凭证绑定信息
     * 手机号
     * 邮箱（安全邮箱）
     * 第三方
     * - 微信
     * - 微博
     * - QQ
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/account/binding/info")
    public Object accountBindingInfo(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountBindingInfo(platformAccountDto, request, response);
    }

    /**
     * 设置密码
     * 手机号短信设置密码
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/password/change")
    public Object changePassword(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.changePassword(platformAccountDto, request, response);
    }

    /**
     * 绑定手机号
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/binding")
    public Object bindingPhone(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.bindingPhone(platformAccountDto, request, response);
    }

    /**
     * 更换手机号 （新手机号和老手机号短信验证码）
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/binding/change")
    public Object changeBindingPhone(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.changeBindingPhone(platformAccountDto, request, response);
    }

    /**
     * 检查手机号状态 （检查手机是否在当前系统使用）
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/check")
    public Object checkPhone(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.checkPhone(platformAccountDto, request, response);
    }

    /**
     * 绑定第三方oauth
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/account/oauth/binding")
    public Object accountBindingOauth(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountBindingOauth(platformAccountDto, request, response);
    }

    /**
     * 解绑第三方oauth
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/account/oauth/untied")
    public Object accountUntiedOauth(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountUntiedOauth(platformAccountDto, request, response);
    }


}
