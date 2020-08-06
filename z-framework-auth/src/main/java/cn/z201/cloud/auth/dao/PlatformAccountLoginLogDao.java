package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformAccountLoginLog;

public interface PlatformAccountLoginLogDao {
    int insertSelective(PlatformAccountLoginLog record);

    PlatformAccountLoginLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformAccountLoginLog record);
}