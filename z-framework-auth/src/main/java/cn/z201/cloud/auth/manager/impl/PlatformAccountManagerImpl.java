package cn.z201.cloud.auth.manager.impl;

import cn.z201.cloud.auth.entity.*;
import cn.z201.cloud.auth.exception.AuthException;
import cn.z201.cloud.auth.dao.PlatformAccountDao;
import cn.z201.cloud.auth.dao.PlatformAccountOauthDao;
import cn.z201.cloud.auth.dao.PlatformAccountRegisteredLogDao;
import cn.z201.cloud.auth.dao.PlatformSystemDao;
import cn.z201.cloud.auth.dto.AuthTokenDto;
import cn.z201.cloud.auth.dto.Result;
import cn.z201.cloud.auth.entity.*;
import cn.z201.cloud.auth.manager.PlatformAccountManagerI;
import cn.z201.cloud.auth.utils.EncryptPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class PlatformAccountManagerImpl implements PlatformAccountManagerI {

    @Autowired
    DataSourceTransactionManager transactionManager;

    @Resource
    PlatformAccountDao platformAccountDao;

    @Resource
    PlatformAccountOauthDao platformAccountOauthDao;

    @Resource
    PlatformAccountRegisteredLogDao platformAccountRegisteredLogDao;

    @Resource
    PlatformSystemDao platformSystemDao;

    @Override
    @Transactional
    public PlatformAccount registeredByOauth(AuthTokenDto authTokenDto,
                                             PlatformSystem platformSystem,
                                             PlatformSystemOauth platformSystemOauth,
                                             String ip) {
        Long time = System.currentTimeMillis();
        PlatformAccount platformAccount = PlatformAccount.builder()
                .isEffective(1)
                .systemId(platformSystem.getId())
                .systemCode(platformSystem.getAccountLibraryCode())
                .updateTime(time)
                .createTime(time)
                .build();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("registeredByOauth-transactional");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int res = platformAccountDao.insertSelective(platformAccount);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
            PlatformAccountOauth platformAccountOauth = PlatformAccountOauth.builder()
                    .accountId(platformAccount.getId())
                    .systemCode(platformSystem.getAccountLibraryCode())
                    .oauthOpenId(authTokenDto.getOpenId())
                    .oauthCode(platformSystemOauth.getOauthType())
                    .oauthName("微信")
                    .oauthId(authTokenDto.getUnionId())
                    .oauthUserName(authTokenDto.getUserInfoDto().getUsername())
                    .isEffective(1)
                    .updateTime(time)
                    .createTime(time)
                    .build();
            res = platformAccountOauthDao.insertSelective(platformAccountOauth);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
            PlatformAccountRegisteredLog platformAccountRegisteredLog = PlatformAccountRegisteredLog.builder()
                    .accountId(platformAccount.getId())
                    .registeredIp(ip)
                    .updateTime(time)
                    .createTime(time)
                    .build();
            res = platformAccountRegisteredLogDao.insertSelective(platformAccountRegisteredLog);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }

        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("registeredByOauth fail , exception.getMessage() {}", e.getMessage());
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试 {} ");
        }
        return platformAccount;
    }

    @Override
    @Transactional
    public PlatformAccount registeredByOauth(PlatformAccountOauth platformAccountOauth,
                                             PlatformSystem platformSystem,
                                             PlatformSystemOauth platformSystemOauth,
                                             String ip) {
        Long time = System.currentTimeMillis();
        PlatformAccount platformAccount = PlatformAccount.builder()
                .isEffective(1)
                .systemId(platformSystem.getId())
                .systemCode(platformSystem.getAccountLibraryCode())
                .updateTime(time)
                .createTime(time)
                .build();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("registeredByOauth-transactional");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int res = platformAccountDao.insertSelective(platformAccount);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
            platformAccountOauth.setAccountId(platformAccount.getId());
            platformAccountOauth.setIsEffective(1);
            platformAccountOauth.setUpdateTime(time);
            res = platformAccountOauthDao.updateByPrimaryKeySelective(platformAccountOauth);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
            PlatformAccountRegisteredLog platformAccountRegisteredLog = PlatformAccountRegisteredLog.builder()
                    .accountId(platformAccount.getId())
                    .registeredIp(ip)
                    .updateTime(time)
                    .createTime(time)
                    .build();
            res = platformAccountRegisteredLogDao.insertSelective(platformAccountRegisteredLog);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("registeredByOauth fail , exception.getMessage() {}", e.getMessage());
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试 {} ");
        }

        return platformAccount;
    }

    @Override
    @Transactional
    public PlatformAccountOauth accountBindingOauth(Long accountId, Integer oauthCode, PlatformSystem platformSystem, AuthTokenDto authTokenDto) {
        Long time = System.currentTimeMillis();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("accountBindingOauth-transactional");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        PlatformAccountOauth platformAccountOauth = PlatformAccountOauth.builder()
                .accountId(accountId)
                .systemCode(platformSystem.getAccountLibraryCode())
                .oauthOpenId(authTokenDto.getOpenId())
                .oauthName("微信")
                .oauthCode(oauthCode)
                .oauthId(authTokenDto.getUnionId())
                .isEffective(1)
                .updateTime(time)
                .createTime(time)
                .build();
        try {
            int res = platformAccountOauthDao.insertSelective(platformAccountOauth);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
            // TODO 日志
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("registeredByOauth fail , exception.getMessage() {}", e.getMessage());
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试 {} ");
        }
        return platformAccountOauth;

    }


    @Override
    @Transactional
    public PlatformAccount registeredByPhone(PlatformSystem platformSystems,
                                             String phone,
                                             String channel,
                                             String ip) {
        String password = phone.substring(phone.length() - 6, phone.length()).toLowerCase();
        Long time = System.currentTimeMillis();
        PlatformAccount platformAccount = PlatformAccount.builder()
                .phoneNumber(phone)
                .password(EncryptPassword.encrypt(password))
                .isEffective(1)
                .systemId(platformSystems.getId())
                .systemCode(platformSystems.getAccountLibraryCode())
                .updateTime(time)
                .createTime(time)
                .build();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("registeredByPhone-transactional");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int res = platformAccountDao.insertSelective(platformAccount);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
            PlatformAccountRegisteredLog platformAccountRegisteredLog = PlatformAccountRegisteredLog.builder()
                    .accountId(platformAccount.getId())
                    .channel(channel)
                    .registeredIp(ip)
                    .updateTime(time)
                    .createTime(time)
                    .build();
            res = platformAccountRegisteredLogDao.insertSelective(platformAccountRegisteredLog);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("registeredByPhone fail , exception.getMessage() {}", e.getMessage());
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试 {} ");
        }
        return platformAccount;
    }

    @Override
    @Transactional
    public PlatformAccount registeredByEmail(PlatformSystem platformSystems,
                                             String email,
                                             String phone,
                                             String pwd,
                                             String ip) {
        Long time = System.currentTimeMillis();
        PlatformAccount platformAccount = PlatformAccount.builder()
                .email(email)
                .phoneNumber(phone)
                .isEffective(1)
                .systemId(platformSystems.getId())
                .password(EncryptPassword.encrypt(pwd))
                .systemCode(platformSystems.getAccountLibraryCode())
                .updateTime(time)
                .createTime(time)
                .build();
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("registeredByEmail-transactional");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            int res = platformAccountDao.insertSelective(platformAccount);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
            PlatformAccountRegisteredLog platformAccountRegisteredLog = PlatformAccountRegisteredLog.builder()
                    .accountId(platformAccount.getId())
                    .registeredIp(ip)
                    .updateTime(time)
                    .createTime(time)
                    .build();
            res = platformAccountRegisteredLogDao.insertSelective(platformAccountRegisteredLog);
            if (0 == res) {
                log.warn("服务器繁忙，请稍后尝试");
                throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试。");
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("registeredByEmail fail , exception.getMessage() {}", e.getMessage());
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "服务器繁忙，请稍后尝试 {} ");
        }
        return platformAccount;
    }

    @Override
    public PlatformSystem findBySystemCode(String systemCode) {
        PlatformSystem platformSystem = platformSystemDao.findBySystemCode(systemCode);
        if (null == platformSystem) {
            log.warn("请求参数不合法，请将参数填写完整");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不完整，请将参数填写完整。");
        }
        return platformSystem;
    }

}
