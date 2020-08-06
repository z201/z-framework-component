package cn.z201.cloud.auth.service.impl;

import cn.z201.cloud.auth.exception.AuthException;
import cn.z201.cloud.auth.service.PlatformAccountServiceI;
import cn.z201.cloud.auth.JwtTokenUtils;
import cn.z201.cloud.auth.dto.Result;
import cn.z201.cloud.auth.dto.TokenInfo;
import cn.z201.cloud.auth.manager.RedisCacheServiceI;
import cn.z201.cloud.auth.manager.impl.PlatformAccountCacheKeyTools;
import cn.z201.cloud.auth.utils.PlatformAccountOnlineCacheKeyTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class PlatformAccountServiceImpl implements PlatformAccountServiceI {

    @Autowired
    RedisCacheServiceI redisCacheService;

    @Autowired
    PlatformAccountCacheKeyTools platformAccountCacheKeyTools;

    @Override
    public void verifyImagesCode(String systemCode, String imageId, String code) {
        String key = platformAccountCacheKeyTools.getAccountImageId(systemCode, imageId);
        String codeCache = redisCacheService.getStr(key);
        if (null == codeCache) {
            log.warn("图片验证码过期了。 imageId {} ", imageId);
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "图形验证码过期,请从新填写。");
        }
        log.info("codeCache {} code {}", codeCache, code);
        if (!codeCache.toLowerCase().equals(code.toLowerCase())) {
            log.warn("请求参数不合法，请将将参数填写完成");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "图形验证码填写错误，或过期。");
        }
    }

    @Override
    public String getJwt(HttpServletRequest request) {
        String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
        if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
            return null;
        }
        String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
        if (Strings.isEmpty(token)) {
            return null;
        }
        return token.trim();
    }

    @Override
    public Long getJwtAccountId(HttpServletRequest request) {
        String token = getJwt(request);
        TokenInfo tokenInfo = JwtTokenUtils.getTokenInfo(token);
        if (null == tokenInfo) {
            log.error("用户未登录");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(),"用户未登录!");
        }
        if (null != tokenInfo.getEnableOnlyOnline()) {
            if (tokenInfo.getEnableOnlyOnline()) {
                String key = PlatformAccountOnlineCacheKeyTools.getAccountOauthUserInfoKey(tokenInfo.getLoginSystemCode(), tokenInfo.getAccountId());
                String online = redisCacheService.getStr(key, String.class);
                if (null == online) {
                    log.error("用户未登录");
                    throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(),"用户未登录!");
                }
                if (!online.equals(token)) {
                    log.error("账号其他设备登录");
                    throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(),"账号在其他设备上登录!");
                }
                // TODO 热刷新
            } else {
                // TODO 热刷新
            }
        }
        return tokenInfo.getAccountId();
    }

    @Override
    public Long getJwtAccountIdNotException(HttpServletRequest request) {
        String token = getJwt(request);
        TokenInfo tokenInfo = JwtTokenUtils.getTokenInfo(token);
        if (null == tokenInfo) {
            return null;
        }
        return tokenInfo.getAccountId();
    }

}
