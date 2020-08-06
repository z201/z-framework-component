package cn.z201.cloud.auth.service;

import cn.z201.cloud.auth.dto.PlatformHttpHeadersDto;

import javax.servlet.http.HttpServletRequest;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformHttpHeadersServiceI {

    PlatformHttpHeadersDto build(HttpServletRequest request);

    PlatformHttpHeadersDto build();
}
