package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformAccountDeviceIdfa;
import org.apache.ibatis.annotations.Param;

public interface PlatformAccountDeviceIdfaDao {
    int insertSelective(PlatformAccountDeviceIdfa record);

    PlatformAccountDeviceIdfa selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformAccountDeviceIdfa record);

    PlatformAccountDeviceIdfa findBySystemCodeAndIdfaId(@Param("systemCode") String systemCode,
                                                        @Param("idfa") String idfa);

}