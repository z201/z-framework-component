package cn.z201.cloud.auth.controller;

import cn.z201.cloud.auth.dto.PlatformAccountDto;
import cn.z201.cloud.auth.service.PlatformAccountAuthServiceI;
import cn.z201.cloud.encrypt.core.annotation.DecryptBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
@RequestMapping(PlatformAccountWebController.ROUTER_INDEX)
public class PlatformAccountWebController {

    static final String ROUTER_INDEX = "/authorization";

    @Autowired
    PlatformAccountAuthServiceI platformAccountAuthService;

    /**
     * 获取图形验证码
     *
     * @param response
     */
    @GetMapping(value = "/verify/image")
    public void verifyImage(HttpServletRequest request, HttpServletResponse response) {
        platformAccountAuthService.verifyImage(request, response);
    }

    /**
     * 判断图片验证码是否正确
     *
     * @param platformAccountDto
     */
    @DecryptBody
    @PostMapping(value = "/verify/image/check")
    public Object verifyImageStatus(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.verifyImageStatus(platformAccountDto, request, response);
    }

    /**
     * 图形验证码登录
     *
     * @param platformAccountDto
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/verify/image/oauth")
    public Object verifyImageCodeLogin(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.verifyImageCodeLogin(platformAccountDto, request, response);
    }

    /**
     * 手机号短信找回密码
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/sms/password/forget")
    public Object smsCodePasswordForget(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.smsCodePasswordForget(platformAccountDto, request, response);
    }

}
