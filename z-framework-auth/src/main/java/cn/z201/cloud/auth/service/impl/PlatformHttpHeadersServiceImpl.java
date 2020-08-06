package cn.z201.cloud.auth.service.impl;

import cn.z201.cloud.auth.service.PlatformHttpHeadersServiceI;
import com.google.gson.Gson;
import cn.z201.cloud.auth.dto.PlatformHttpHeadersDto;
import cn.z201.cloud.auth.utils.PlatformHttpHeadersKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Service
public class PlatformHttpHeadersServiceImpl implements PlatformHttpHeadersServiceI {

    @Override
    public PlatformHttpHeadersDto build(HttpServletRequest request) {
        String businessGroupSource = request.getHeader(PlatformHttpHeadersKey.CLIENT_BUSINESS_GROUP_SOURCE);
        String clientBusinessSource = request.getHeader(PlatformHttpHeadersKey.CLIENT_BUSINESS_SOURCE);
        String clientBusinessActivitySource = request.getHeader(PlatformHttpHeadersKey.CLIENT_BUSINESS_ACTIVITY_SOURCE);
        String clientEnvSource = request.getHeader(PlatformHttpHeadersKey.CLIENT_EVN_SOURCE);
        String clientPlatformSource = request.getHeader(PlatformHttpHeadersKey.CLIENT_PLATFORM_SOURCE);
        String clientStartTime = request.getHeader(PlatformHttpHeadersKey.CLIENT_START_TIME);
        String clientVersionSource = request.getHeader(PlatformHttpHeadersKey.CLIENT_VERSION_SOURCE);
        String clientIp = request.getHeader(PlatformHttpHeadersKey.X_REAL_IP);
        PlatformHttpHeadersDto platformHttpHeadersDto = PlatformHttpHeadersDto.builder()
                .clientBusinessGroupSource(businessGroupSource)
                .clientBusinessSource(clientBusinessSource)
                .clientBusinessActivitySource(clientBusinessActivitySource)
                .clientEnvSource(clientEnvSource)
                .clientPlatformSource(clientPlatformSource)
                .clientStartTime(clientStartTime)
                .clientVersionSource(clientVersionSource)
                .clientIp(clientIp)
                .build();
        if (log.isDebugEnabled()) {
            log.debug("HttpHeaders   {}", new Gson().toJson(platformHttpHeadersDto));
        }
        return platformHttpHeadersDto;
    }

    @Override
    public PlatformHttpHeadersDto build() {
        ServletRequestAttributes httpServletRequest = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (null == httpServletRequest) {
            return null;
        }
        return build(httpServletRequest.getRequest());
    }
}
