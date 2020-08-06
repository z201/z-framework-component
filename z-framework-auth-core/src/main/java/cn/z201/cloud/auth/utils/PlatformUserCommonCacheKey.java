package cn.z201.cloud.auth.utils;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformUserCommonCacheKey {

    /**
     * 30天秒钟 60 * 60 * 24 * 30
     */
    Long PLATFORM_USER_INFO_CACHE_EXPIRATION = 2592000L;

    String PLATFORM_USER_KEY = "platform:user:";

    String PLATFORM_USER_PROFILE_INFO_KEY = PLATFORM_USER_KEY + "profile";

    String PLATFORM_USER_PROFILE_INFO_KEY_TEMP = PLATFORM_USER_PROFILE_INFO_KEY + ":%s:%s";

    /**
     * 获取用户基础缓存key
     * platform:user:profile:${v}-${userId}
     * @param systemCode
     * @param userNumber
     * @return
     */
    String getUsUserProfileInfoKey(String systemCode, String userNumber);


    String PLATFORM_USER_AUTH_INFO_KEY = PLATFORM_USER_KEY + "auth";


    String PLATFORM_USER_AUTH_INFO_KEY_TEMP = PLATFORM_USER_AUTH_INFO_KEY + ":%s:%s";




}
