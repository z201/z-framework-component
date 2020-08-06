package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformAccountUpdateLog;

public interface PlatformAccountUpdateLogDao {
    int insertSelective(PlatformAccountUpdateLog record);

    PlatformAccountUpdateLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformAccountUpdateLog record);
}