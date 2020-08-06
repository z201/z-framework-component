package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformSystem;
import org.apache.ibatis.annotations.Param;

public interface PlatformSystemDao {
    int insertSelective(PlatformSystem record);

    PlatformSystem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformSystem record);

    PlatformSystem findBySystemCode(@Param("systemCode") String systemCode);
}