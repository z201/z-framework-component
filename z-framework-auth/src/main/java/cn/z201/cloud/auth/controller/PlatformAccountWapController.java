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
@RequestMapping(PlatformAccountWapController.ROUTER_INDEX)
public class PlatformAccountWapController {

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
    @PostMapping(value = "/account/wap/oauth")
    public Object accountWapOauth(@RequestBody PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        return platformAccountAuthService.accountWapOauth(platformAccountDto, request, response);
    }

}
