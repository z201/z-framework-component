package cn.z201.cloud.auth.manager.impl;

import cn.z201.cloud.auth.manager.PlatformAccountCacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Service
public class PlatformAccountCacheKeyTools implements PlatformAccountCacheKey {

    @Override
    public String getAccountOauthUserInfoKey(String code) {
        return String.format(ACCOUNT_OAUTH_USER_INFO_KEY_TEMP, code);
    }

    @Override
    public String getAccountSmsCodeCacheKey(String systemCode, String phone, Integer smsType) {
        return String.format(ACCOUNT_SMS_CODE_CACHE_KEY_TEMP, systemCode, phone, smsType);
    }

    @Override
    public String getAccountImageId(String systemCode, String id) {
        return String.format(ACCOUNT_IMAGE_ID_TEMP, systemCode, id);
    }

    @Override
    public String getAccountSessionKey(String systemCode, String openId) {
        return String.format(ACCOUNT_SESSION_KEY_TEMP, systemCode, openId);
    }

    @Override
    public String getAccountAuthInfoKey(Long accountId) {
        return String.format(ACCOUNT_AUTH_INFO_KEY_TEMP, String.valueOf(accountId));
    }

    @Override
    public String getAccountClientInfoKey(Long accountId) {
        return String.format(ACCOUNT_CLIENT_INFO_KEY_TEMP, String.valueOf(accountId));
    }


}
