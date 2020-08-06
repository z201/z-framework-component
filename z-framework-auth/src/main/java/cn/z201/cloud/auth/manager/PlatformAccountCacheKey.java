package cn.z201.cloud.auth.manager;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountCacheKey {

    /**
     * 全局默认过期时间
     */
    Long EXPIRATION = 300L;

    /**
     * oauth:user:info:temp:${code}
     * 用户第三方临时缓存,方便注册
     * key code
     * value json(userInfo)
     */
    String ACCOUNT_OAUTH_USER_INFO_KEY = "account:oauth:info:";

    String ACCOUNT_OAUTH_USER_INFO_KEY_TEMP = ACCOUNT_OAUTH_USER_INFO_KEY + ":%s";

    /**
     * 获取第三方注册用户信息
     *
     * @param code
     * @return
     */
    String getAccountOauthUserInfoKey(String code);

    /**
     * 账号短信
     * account:sms:code:${systemCode}:${phone}-{smsType}
     */
    String ACCOUNT_SMS_CODE_CACHE_KEY = "account:sms:code";

    String ACCOUNT_SMS_CODE_CACHE_KEY_TEMP = ACCOUNT_SMS_CODE_CACHE_KEY + ":%s:%s-%s";

    /**
     * 获取账号短信
     *
     * @param systemCode
     * @param phone
     * @param smsType
     * @return
     */
    String getAccountSmsCodeCacheKey(String systemCode, String phone, Integer smsType);

    /**
     * 账号图形验证码
     * account:verify:image:{systemCode}-{imageId}
     */
    String ACCOUNT_IMAGE_ID = "account:verify:image:id";

    String ACCOUNT_IMAGE_ID_TEMP = ACCOUNT_IMAGE_ID + ":%s-%s";

    /**
     * 获取图片id缓存
     *
     * @param id
     * @return
     */
    String getAccountImageId(String systemCode, String id);

    /**
     * 小程序注册
     * account:session:key{systemCode}-{openId}
     */
    String ACCOUNT_SESSION_KEY = "account:session:key";

    String ACCOUNT_SESSION_KEY_TEMP = ACCOUNT_SESSION_KEY+":%s-%s";

    /**
     * 获取sessionKey
     *
     * @param systemCode
     * @param sessionKey
     * @return
     */
    String getAccountSessionKey(String systemCode, String sessionKey);


    String ACCOUNT_AUTH_INFO_KEY = "account:auth:info";

    String ACCOUNT_AUTH_INFO_KEY_TEMP = ACCOUNT_AUTH_INFO_KEY+":%s";

    /**
     * 账号安全信息
     * @param accountId
     * @return
     */
    String getAccountAuthInfoKey(Long accountId);


    String ACCOUNT_CLIENT_INFO_KEY = "account:client:info";

    String ACCOUNT_CLIENT_INFO_KEY_TEMP = ACCOUNT_CLIENT_INFO_KEY+":%s";

    /**
     * 账号安全信息
     * @param accountId
     * @return
     */
    String getAccountClientInfoKey(Long accountId);




}
