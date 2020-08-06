package cn.z201.cloud.auth.manager.impl;

import cn.z201.cloud.auth.annotation.AuthManagerHandler;
import cn.z201.cloud.auth.dto.AuthTokenDto;
import cn.z201.cloud.auth.dto.AuthUserInfoDto;
import cn.z201.cloud.auth.dto.PlatformAccountDto;
import cn.z201.cloud.auth.dto.Result;
import cn.z201.cloud.auth.entity.PlatformSystemOauth;
import cn.z201.cloud.auth.enums.AuthUserGenderEnum;
import cn.z201.cloud.auth.exception.AuthException;
import cn.z201.cloud.auth.manager.AuthManagerI;
import cn.z201.cloud.auth.manager.RestTemplateManagerI;
import cn.z201.cloud.auth.utils.PlatformAccountOauthManagerI;
import com.alibaba.fastjson.JSONObject;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@AuthManagerHandler(type = 1)
@Component
public class AuthAppWeChatManagerImpl implements AuthManagerI, PlatformAccountOauthManagerI {

    @Autowired
    RestTemplateManagerI restTemplateManager;

    @Override
    public AuthTokenDto oAuth(PlatformAccountDto platformAccountDto, PlatformSystemOauth platformSystemOauth) {
        AuthTokenDto authTokenDto = new AuthTokenDto();
        String code = platformAccountDto.getCode();
        String url = String.format(APP_WX_OAUTH_URL, platformSystemOauth.getKey(), platformSystemOauth.getSecretId(), code);
        log.info("wx oauth url {} ", url);
        RestTemplate restTemplate = restTemplateManager.defHttps();
        ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
        if (res.getStatusCode().equals(HttpStatus.OK)) {
            JSONObject jsonObject = JSONObject.parseObject(res.getBody());
            checkResponse(jsonObject);
            String accessToken = jsonObject.getString("access_token");
            String openId = jsonObject.getString("openid");
            String refreshToken = jsonObject.getString("refresh_token");
            Integer expiresIn = jsonObject.getIntValue("expires_in");
            authTokenDto.setAccessToken(accessToken);
            authTokenDto.setOpenId(openId);
            authTokenDto.setRefreshToken(refreshToken);
            authTokenDto.setExpireIn(expiresIn);
            StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
            restTemplate = new RestTemplateBuilder().additionalMessageConverters(m).build();
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            url = String.format(APP_WX_OAUTH_USER_INFO_URL, accessToken, openId);
            res = restTemplate.getForEntity(url, String.class, headers);
            if (res.getStatusCode().equals(HttpStatus.OK)) {
                jsonObject = JSONObject.parseObject(res.getBody());
                checkResponse(jsonObject);
                String location = String.format("%s-%s-%s", jsonObject.getString("country"), jsonObject.getString("province"), jsonObject.getString("city"));
                String unionid = jsonObject.getString("unionid");
                String nickname = jsonObject.getString("nickname");
                String headimgurl="";
                if(jsonObject.containsKey("headimgurl") && jsonObject.get("headimgurl") != null) {
                    headimgurl = jsonObject.getString("headimgurl").replace("/132","/0");
                }
                String sex = jsonObject.getString("sex");
                openId = jsonObject.getString("openid");
                authTokenDto.setOpenId(openId);
                authTokenDto.setUnionId(unionid);
                if (null != nickname && !nickname.isEmpty()) {
                    nickname = EmojiParser.removeAllEmojis(nickname);
                }
                if (null != openId && !openId.isEmpty()) {
                    authTokenDto.setOpenId(openId);
                }
                AuthUserInfoDto authUserInfoDto = AuthUserInfoDto.builder()
                        .username(nickname)
                        .nickname(nickname)
                        .avatar(headimgurl)
                        .location(location)
                        .openIp(openId)
                        .gender(AuthUserGenderEnum.getRealGender(sex).getDesc())
                        .sourceType(3)
                        .build();
                authTokenDto.setUserInfoDto(authUserInfoDto);
                return authTokenDto;
            }
            log.warn("wechat 应用授权登录失败 code 检查成功后获取用户数据失败 ");
            throw new AuthException(Result.Code.ERROR.getCode(),
                    "wechat 应用授权登录失败，请从新尝试。",
                    "wechat 应用授权登录失败 code 检查成功后获取用户数据失败");
        }
        log.warn("wechat 应用授权登录失败 code 检查失败 {}", res.getStatusCode());
        throw new AuthException(Result.Code.ERROR.getCode(),
                "wechat 应用授权登录失败，请从新尝试。",
                "wechat 应用授权登录失败 code 检查失败");
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
