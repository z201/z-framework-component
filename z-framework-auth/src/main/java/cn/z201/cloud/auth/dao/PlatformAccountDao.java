package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformAccountDao {
    int insertSelective(PlatformAccount record);

    PlatformAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformAccount record);

    List<PlatformAccount> listAll();
    /**
     * 根据系统手机号和系统编号获取账号
     * @param phone
     * @param systemCode
     * @return
     */
    PlatformAccount findByPhoneAndSystemCode(@Param("phone") String phone, @Param("systemCode") String systemCode);

    /**
     * 根据手机号或者邮箱登录
     * @param account
     * @param systemCode
     * @return
     */
    PlatformAccount findByAccountAndSystemCode(@Param("account") String account, @Param("systemCode") String systemCode);

    /**
     * 检查手机号
     * @param phone
     * @param systemCode
     * @return
     */
    String existsPhone(@Param("phone") String phone, @Param("systemCode") String systemCode);

    /**
     * 根据邮箱或者邮箱登录
     * @param email
     * @param systemCode
     * @return
     */
    PlatformAccount findByEmailAndSystemCode(@Param("email") String email, @Param("systemCode") String systemCode);

    /**
     * 检查email是否有人使用
     * @param email
     * @param systemCode
     * @return
     */
    String existsEmail(@Param("email") String email, @Param("systemCode") String systemCode);

}