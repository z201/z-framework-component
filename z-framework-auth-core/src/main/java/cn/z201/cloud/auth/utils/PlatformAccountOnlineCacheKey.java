package cn.z201.cloud.auth.utils;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountOnlineCacheKey {

    /**
     * oauth:user:online:${systemCode}:${accountId}
     * 用户登录缓存
     * value issueTime 签发时间
     */
    String ACCOUNT_OAUTH_USER_INFO_KEY = "platform:account:oauth:online:";

    String ACCOUNT_OAUTH_USER_INFO_KEY_TEMP = ACCOUNT_OAUTH_USER_INFO_KEY+"%s:%s";

    String ACCOUNT_AUTH_INFO_KEY = "account:auth:info";

    String ACCOUNT_AUTH_INFO_KEY_TEMP = ACCOUNT_AUTH_INFO_KEY+":%s";



}
