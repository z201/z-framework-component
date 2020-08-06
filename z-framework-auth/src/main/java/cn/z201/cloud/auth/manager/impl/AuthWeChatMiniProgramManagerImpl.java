package cn.z201.cloud.auth.manager.impl;

import cn.z201.cloud.auth.exception.AuthException;
import com.alibaba.fastjson.JSONObject;
import cn.z201.cloud.auth.annotation.AuthManagerHandler;
import cn.z201.cloud.auth.dto.AuthTokenDto;
import cn.z201.cloud.auth.dto.PlatformAccountDto;
import cn.z201.cloud.auth.dto.Result;
import cn.z201.cloud.auth.entity.PlatformSystemOauth;
import cn.z201.cloud.auth.manager.AuthManagerI;
import cn.z201.cloud.auth.manager.RestTemplateManagerI;
import cn.z201.cloud.auth.utils.PlatformAccountOauthManagerI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@AuthManagerHandler(type = 2)
@Component
public class AuthWeChatMiniProgramManagerImpl implements AuthManagerI, PlatformAccountOauthManagerI {

    @Autowired
    RestTemplateManagerI restTemplateManager;

    @Override
    public AuthTokenDto oAuth(PlatformAccountDto platformAccountDto, PlatformSystemOauth platformSystemOauth) {
        AuthTokenDto authTokenDto = new AuthTokenDto();
        String code = platformAccountDto.getCode();
        String url = String.format(MINI_APP_WX_OAUTH_URL, platformSystemOauth.getKey(), platformSystemOauth.getSecretId(), code);
        log.info("wx oauth url {} ", url);
        RestTemplate restTemplate = restTemplateManager.defHttps();
        ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
        if (res.getStatusCode().equals(HttpStatus.OK)) {
            JSONObject jsonObject = JSONObject.parseObject(res.getBody());
            checkResponse(jsonObject);
            String sessionKey = jsonObject.getString("session_key");
            String openId = jsonObject.getString("openid");
            String unionId = jsonObject.getString("unionid");
            authTokenDto.setOpenId(openId);
            authTokenDto.setSessionKey(sessionKey);
            if (null != unionId) {
                authTokenDto.setUnionId(unionId);
            }
            return authTokenDto;
        }
        log.warn("wechat 应用授权登录失败 code 检查成功后获取用户数据失败 ");
        throw new AuthException(Result.Code.ERROR.getCode(),
                "wechat 应用授权登录失败，请从新尝试。",
                "wechat 应用授权登录失败 code 检查成功后获取用户数据失败");
    }

    /**
     * 检查响应内容是否正确
     *
     * @param object 请求响应内容
     */
    private void checkResponse(JSONObject object) {
        if (object.containsKey("errcode")) {
            Integer errcode = object.getInteger("errcode");
            String errmsg = object.getString("errmsg");
            log.warn("wechat 应用授权登录失败   errcode {} errmsg {} ", errcode, errmsg);
            throw new AuthException(object.getIntValue("errcode"), object.getString("errmsg"));
        }
    }
}
