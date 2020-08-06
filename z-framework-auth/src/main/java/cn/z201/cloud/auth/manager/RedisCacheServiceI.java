package cn.z201.cloud.auth.manager;

import java.util.List;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
public interface RedisCacheServiceI {

    /**
     * 设置过期的字符串
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    Boolean setStr(String key, String value, Long timeout);

    /**
     * 设置不过期的字符串
     *
     * @param key
     * @param value
     * @return
     */
    Boolean setStr(String key, String value);


    /**
     * 获取字符串key的value
     *
     * @param key
     * @return
     */
    String getStr(String key);

    /**
     * 获取字符串key的value
     *
     * @param key
     * @return
     */
    <T> T getStr(String key, Class<T> clazz);


    <T, R> List<T> mGet(List<R> list, Class<T> clazz);

    /**
     * 根据key删除缓存
     *
     * @param key
     * @return
     */
    Long del(String key);

    /**
     * hmSet
     *
     * @param key
     * @param m
     */
    void hSet(String key, Map m);

    /**
     * hget
     *
     * @param key
     * @param hashKey
     * @return
     */
    Object hGet(String key, String hashKey);
}
