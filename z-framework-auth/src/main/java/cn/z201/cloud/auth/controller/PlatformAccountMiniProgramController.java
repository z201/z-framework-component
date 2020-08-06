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
@RequestMapping(PlatformAccountMiniProgramController.ROUTER_INDEX)
public class PlatformAccountMiniProgramController {

    static final String ROUTER_INDEX = "/authorization";

    @Autowired
    PlatformAccountAuthServiceI platformAccountAuthService;

    /**
     * 小程序oauth授权
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/account/mini/program/oauth")
    public Object accountMiniProgramOauth(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountMiniProgramOauth(platformAccountDto, request, response);
    }

    /**
     * 小程序oauth授权注册
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/account/mini/program/registered")
    public Object accountMiniProgramRegistered(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountMiniProgramRegistered(platformAccountDto, request, response);
    }

    /**
     * 小程序oauth获取手机号信息并绑定
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/account/mini/program/phone/binding")
    public Object accountMiniProgramPhoneBinding(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountMiniProgramPhoneBinding(platformAccountDto, request, response);
    }

    /**
     * 小程序oauth准备获取手机号信息并绑定
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/account/mini/program/read/phone/binding")
    public Object accountMiniProgramPhoneReadyPhoneBinding(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountMiniProgramPhoneReadyPhoneBinding(platformAccountDto, request, response);
    }



}
