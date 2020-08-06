package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformSystemOauth;
import org.apache.ibatis.annotations.Param;

public interface PlatformSystemOauthDao {
    int insertSelective(PlatformSystemOauth record);

    PlatformSystemOauth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformSystemOauth record);

    PlatformSystemOauth findBySystemCodeAndOauthType(@Param("systemCode") String systemCode ,
                                                   @Param("oauthType") Integer oauthType);


}