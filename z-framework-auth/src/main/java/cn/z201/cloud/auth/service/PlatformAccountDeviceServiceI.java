package cn.z201.cloud.auth.service;

import cn.z201.cloud.auth.dto.PlatformAccountDeviceDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountDeviceServiceI {

    /**
     * 苹果手机idfa
     * @param platformAccountDeviceDto
     * @param request
     * @param response
     * @return
     */
    Object idfa(PlatformAccountDeviceDto platformAccountDeviceDto,
                HttpServletRequest request,
                HttpServletResponse response);

    /**
     * umeng
     * @param platformAccountDeviceDto
     * @param request
     * @param response
     * @return
     */
    Object umeng(PlatformAccountDeviceDto platformAccountDeviceDto,
                HttpServletRequest request,
                HttpServletResponse response);
}
