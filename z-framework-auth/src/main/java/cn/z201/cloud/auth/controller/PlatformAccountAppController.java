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
@RequestMapping(PlatformAccountAppController.ROUTER_INDEX)
public class PlatformAccountAppController {

    static final String ROUTER_INDEX = "/authorization";

    @Autowired
    PlatformAccountAuthServiceI platformAccountAuthService;

    /**
     * 第三方oauth授权
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/account/oauth")
    public Object accountOauth(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountOauth(platformAccountDto, request, response);
    }

    /**
     * 手机短信登录
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/sms/oauth")
    public Object phoneSmsLogin(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.phoneSmsLogin(platformAccountDto, request, response);
    }

    /**
     * 手机号密码登录
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/phone/password/oauth")
    public Object phonePasswordLogin(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.phonePasswordLogin(platformAccountDto, request, response);
    }


}
