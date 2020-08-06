package cn.z201.cloud.auth.manager;

import cn.z201.cloud.auth.dto.PlatformUserMessageDataSourceDto;

/**
 * @author z201.coding@gmail.com
 **/
public interface PlatformAccountDeviceManagerI {

    PlatformUserMessageDataSourceDto getAccountDevice(String systemCode,Long accountId);

    void refreshAccountDevice(Long accountId);
}
