package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformAccountOauth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformAccountOauthDao {
    int insertSelective(PlatformAccountOauth record);

    PlatformAccountOauth selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformAccountOauth record);

    PlatformAccountOauth findByOauthIdAndSystemCodeAndOauthCode(@Param("oauthId") String oauthId,
                                                                @Param("systemCode") String systemCode,
                                                                @Param("oauthCode") Integer oauthCode);

    List<PlatformAccountOauth> listByOauthIdAndSystemCode(@Param("oauthId") String oauthId,
                                                          @Param("systemCode") String systemCode);


    List<PlatformAccountOauth> listByAccountIdAndSystemCode(@Param("accountId") Long accountId,
                                                            @Param("systemCode") String systemCode);

    Integer updateEffectiveAccountIdAndSystemCode(@Param("accountId") Long accountId,
                                                  @Param("systemCode") String systemCode,
                                                  @Param("effective") Integer effective);

    List<PlatformAccountOauth> listByAccountId(@Param("accountId") Long accountId);

}