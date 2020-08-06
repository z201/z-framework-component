package cn.z201.cloud.auth.manager;

import cn.z201.cloud.auth.dto.AuthUserInfoDto;
import cn.z201.cloud.auth.entity.PlatformAccount;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountCacheManagerI {

    /**
     * 刷新平台账号缓存信息
     *
     * @param platformAccount
     * @return
     */
    AuthUserInfoDto refreshPlatformAccountCache(PlatformAccount platformAccount);

    /**
     * 刷新平台账号缓存信息
     *
     * @param accountId
     * @return
     */
    AuthUserInfoDto refreshPlatformAccountCache(Long accountId);

    /**
     * 获取平台账号缓存信息
     *
     * @param accountId
     * @return
     */
    AuthUserInfoDto getPlatformAccountCache(Long accountId);

    /**
     * 批量获取账号安全缓存信息
     *
     * @param accountIds
     * @return
     */
    List<AuthUserInfoDto> listLimitPlatformAccountCache(List<Long> accountIds);

}
