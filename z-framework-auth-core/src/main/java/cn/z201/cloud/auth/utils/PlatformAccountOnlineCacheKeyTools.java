package cn.z201.cloud.auth.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
public class PlatformAccountOnlineCacheKeyTools implements PlatformAccountOnlineCacheKey {

    /**
     *
     * @param systemCode
     * @param accountId
     * @return
     */
    public static String getAccountOauthUserInfoKey(String systemCode, Long accountId) {
        String key = String.format(ACCOUNT_OAUTH_USER_INFO_KEY_TEMP, systemCode, String.valueOf(accountId));
        if (log.isDebugEnabled()) {
            log.debug("accountOauthUserInfoKey {} ", key);
        }
        return key;
    }

    /**
     * 账号安全信息
     *
     * @param accountId
     * @return
     */

    public static String getAccountAuthInfoKey(Long accountId) {
        String key = String.format(ACCOUNT_AUTH_INFO_KEY_TEMP, String.valueOf(accountId));
        if (log.isDebugEnabled()) {
            log.debug("accountAuthInfoKey {} ", key);
        }
        return key;
    }

}
