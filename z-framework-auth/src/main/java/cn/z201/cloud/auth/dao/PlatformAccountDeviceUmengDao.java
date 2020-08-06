package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformAccountDeviceUmeng;
import org.apache.ibatis.annotations.Param;

public interface PlatformAccountDeviceUmengDao {
    int insertSelective(PlatformAccountDeviceUmeng record);

    PlatformAccountDeviceUmeng selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformAccountDeviceUmeng record);

    PlatformAccountDeviceUmeng findByAccountId(@Param("accountId") Long accountId);

    PlatformAccountDeviceUmeng findByDeviceId(@Param("deviceId") String deviceId);

}