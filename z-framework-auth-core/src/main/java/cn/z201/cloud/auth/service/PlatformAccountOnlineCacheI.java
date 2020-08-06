package cn.z201.cloud.auth.service;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountOnlineCacheI {

    /**
     * 账户上线
     *
     * @param key
     * @param jwt
     * @return
     */
    boolean accountOnline(String key, String jwt);

    /**
     * 账号下线
     * @param key
     * @param jwt
     * @return
     */
    boolean accountOffline(String key, String jwt);

    /**
     * 刷新账号上线状态
     *
     * @param key    缓存
     * @param oldJwt 原始jwt
     * @param jwt    新jwt
     * @return
     */
    boolean refreshAccountOnline(String key, String oldJwt, String jwt);

}
