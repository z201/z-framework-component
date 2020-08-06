package cn.z201.cloud.auth.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class PlatformUserCommonCacheKeyTools implements PlatformUserCommonCacheKey {

    @Override
    public String getUsUserProfileInfoKey(String systemCode, String id) {
        return String.format(PLATFORM_USER_PROFILE_INFO_KEY_TEMP, systemCode, id);
    }

}
