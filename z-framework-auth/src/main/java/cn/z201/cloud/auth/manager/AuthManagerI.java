package cn.z201.cloud.auth.manager;

import cn.z201.cloud.auth.dto.AuthTokenDto;
import cn.z201.cloud.auth.dto.PlatformAccountDto;
import cn.z201.cloud.auth.entity.PlatformSystemOauth;

/**
 * @author z201.coding@gmail.com
 **/
public interface AuthManagerI {

    /**
     * 授权
     * @param platformAccountDt
     * @param platformSystemOauth
     * @return
     */
    AuthTokenDto oAuth(PlatformAccountDto platformAccountDt, PlatformSystemOauth platformSystemOauth);


}
