package cn.z201.cloud.gateway.service.impl;

import cn.z201.cloud.gateway.service.AuthServiceI;
import cn.z201.cloud.gateway.property.VlinkGatewayProperty;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * @author z201.coding@gmail.com
 **/
@Service
public class AuthServiceImpl implements AuthServiceI {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    VlinkGatewayProperty vlinkGatewayProperty;


    @Override
    public boolean ignoreAuthentication(String url) {
        return Stream.of(vlinkGatewayProperty.getIgnoreUrlStartWith().split(",")).anyMatch(ignoreUrl -> url.startsWith(StringUtils.trim(ignoreUrl)));
    }

    @Override
    public boolean invalidJwtAccessToken(String authentication) {
        // TODO 代码没写完
        return true;
    }

    @Override
    public Object getJwt(String authentication) {
        // TODO 代码没写完
        return null;
    }
}
