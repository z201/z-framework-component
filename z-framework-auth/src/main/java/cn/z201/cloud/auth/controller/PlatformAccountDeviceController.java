package cn.z201.cloud.auth.controller;

import cn.z201.cloud.auth.dto.PlatformAccountDeviceDto;
import cn.z201.cloud.auth.service.PlatformAccountDeviceServiceI;
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
@RequestMapping(PlatformAccountDeviceController.ROUTER_INDEX)
public class PlatformAccountDeviceController {

    static final String ROUTER_INDEX = "/authorization/device";

    @Autowired
    PlatformAccountDeviceServiceI platformAccountDeviceService;

    /**
     * 苹果手机idfa
     *
     * @param platformAccountDeviceDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/idfa")
    public Object idfa(@RequestBody PlatformAccountDeviceDto platformAccountDeviceDto,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        return platformAccountDeviceService.idfa(platformAccountDeviceDto,request,response);
    }

    /**
     * 友盟
     * umeng
     *
     * @param platformAccountDeviceDto
     * @param request
     * @param response
     * @return
     */
    @DecryptBody
    @PostMapping(value = "/umeng")
    public Object umeng(@RequestBody PlatformAccountDeviceDto platformAccountDeviceDto,
                 HttpServletRequest request,
                 HttpServletResponse response){
        return platformAccountDeviceService.umeng(platformAccountDeviceDto,request,response);
    }


}
