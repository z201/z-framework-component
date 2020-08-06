package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformAccountRegisteredLog;

public interface PlatformAccountRegisteredLogDao {
    int insertSelective(PlatformAccountRegisteredLog record);

    PlatformAccountRegisteredLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformAccountRegisteredLog record);
}