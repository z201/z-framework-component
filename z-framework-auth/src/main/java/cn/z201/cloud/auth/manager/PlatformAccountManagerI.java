package cn.z201.cloud.auth.manager;

import cn.z201.cloud.auth.dto.AuthTokenDto;
import cn.z201.cloud.auth.entity.PlatformAccount;
import cn.z201.cloud.auth.entity.PlatformAccountOauth;
import cn.z201.cloud.auth.entity.PlatformSystem;
import cn.z201.cloud.auth.entity.PlatformSystemOauth;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountManagerI {

    /**
     * 第三方注册
     *
     * @param authTokenDto
     * @param platformSystem
     * @param platformSystemOauth
     * @param ip
     * @return
     */
    PlatformAccount registeredByOauth(AuthTokenDto authTokenDto,
                                      PlatformSystem platformSystem,
                                      PlatformSystemOauth platformSystemOauth,
                                      String ip);




    /**
     *
     * 第三方注册
     *
     * @param platformAccountOauth
     * @param platformSystem
     * @param platformSystemOauth
     * @param ip
     * @return
     */
    PlatformAccount registeredByOauth(PlatformAccountOauth platformAccountOauth,
                                      PlatformSystem platformSystem,
                                      PlatformSystemOauth platformSystemOauth,
                                      String ip);

    /**
     * 账号绑定
     *
     * @param accountId
     * @param oauthCode
     * @param platformSystem
     * @param authTokenDto
     * @return
     */
    PlatformAccountOauth accountBindingOauth(Long accountId,
                                             Integer oauthCode,
                                             PlatformSystem platformSystem,
                                             AuthTokenDto authTokenDto);

    /**
     * 手机号注册
     *
     * @param platformSystems 系统信息
     * @param phone           手机号
     * @param channel         安卓的注册渠道
     * @param ip              注册ip
     * @return
     */
    PlatformAccount registeredByPhone(PlatformSystem platformSystems,
                                      String phone,
                                      String channel,
                                      String ip);

    /**
     * 手机号注册
     *
     * @param platformSystems 系统信息
     * @param account         账号
     * @param phone           手机号
     * @param pwd           手机号
     * @param ip              注册ip
     * @return
     */
    PlatformAccount registeredByEmail(PlatformSystem platformSystems,
                                      String account,
                                      String phone,
                                      String pwd,
                                      String ip);

    /**
     * 获取平台数据
     * @param systemCode
     * @return
     */
    PlatformSystem findBySystemCode(String systemCode);
}
