package cn.z201.cloud.auth.service.impl;

import cn.z201.cloud.auth.dao.PlatformAccountDeviceIdfaDao;
import cn.z201.cloud.auth.dao.PlatformAccountDeviceUmengDao;
import cn.z201.cloud.auth.dao.PlatformSystemDao;
import cn.z201.cloud.auth.dto.PlatformAccountDeviceDto;
import cn.z201.cloud.auth.dto.PlatformHttpHeadersDto;
import cn.z201.cloud.auth.dto.Result;
import cn.z201.cloud.auth.entity.PlatformAccountDeviceIdfa;
import cn.z201.cloud.auth.entity.PlatformAccountDeviceUmeng;
import cn.z201.cloud.auth.entity.PlatformSystem;
import cn.z201.cloud.auth.exception.AuthException;
import cn.z201.cloud.auth.manager.PlatformAccountCacheKey;
import cn.z201.cloud.auth.manager.PlatformAccountDeviceManagerI;
import cn.z201.cloud.auth.manager.PlatformAccountManagerI;
import cn.z201.cloud.auth.manager.RedisCacheServiceI;
import cn.z201.cloud.auth.service.PlatformAccountServiceI;
import cn.z201.cloud.auth.service.PlatformHttpHeadersServiceI;
import cn.z201.cloud.auth.utils.ParamCheckUtil;
import cn.z201.cloud.auth.service.PlatformAccountDeviceServiceI;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z201.coding@gmail.com
 **/
@Service
@Slf4j
public class PlatformAccountDeviceServiceImpl implements PlatformAccountDeviceServiceI {

    @Autowired
    PlatformAccountManagerI platformAccountManager;

    @Autowired
    PlatformAccountServiceI platformAccountService;

    @Autowired
    PlatformHttpHeadersServiceI platformHttpHeadersService;

    @Autowired
    RedisCacheServiceI redisCacheService;

    @Autowired
    PlatformAccountCacheKey platformAccountCacheKey;

    @Autowired
    PlatformAccountDeviceManagerI platformAccountDeviceManager;

    @Resource
    PlatformAccountDeviceUmengDao platformAccountDeviceUmengDao;

    @Resource
    PlatformAccountDeviceIdfaDao platformAccountDeviceIdfaDao;

    @Resource
    PlatformSystemDao platformSystemDao;

    @Override
    public Object idfa(PlatformAccountDeviceDto platformAccountDeviceDto,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        Long accountId = platformAccountService.getJwtAccountIdNotException(request);
        String idfa = platformAccountDeviceDto.getIdfaDeviceId();
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(code, ip, idfa)) {
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不完整！");
        }
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        if (null == platformSystem) {
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不完整！");
        }
        PlatformAccountDeviceIdfa platformAccountDeviceIdfa = platformAccountDeviceIdfaDao.findBySystemCodeAndIdfaId(platformSystem.getAccountLibraryCode(), idfa);
        if (null == platformAccountDeviceIdfa) {
            Long time = System.currentTimeMillis();
            platformAccountDeviceIdfa = PlatformAccountDeviceIdfa.builder().build();
            platformAccountDeviceIdfa.setSystemCode(platformSystem.getAccountLibraryCode());
            platformAccountDeviceIdfa.setIdfa(idfa);
            platformAccountDeviceIdfa.setPlatformAccountId(accountId);
            platformAccountDeviceIdfa.setIsDel(false);
            platformAccountDeviceIdfa.setCreateTime(time);
            platformAccountDeviceIdfa.setUpdateTime(time);
            int res = platformAccountDeviceIdfaDao.insertSelective(platformAccountDeviceIdfa);
            if (0 == res) {
                return Result.error(Result.Code.NO_UPDATE.getCode(), "操作失败");
            }
        }
        return Result.success();
    }

    @Override
    public Object umeng(PlatformAccountDeviceDto platformAccountDeviceDto,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String clientBusinessSource = platformHttpHeadersDto.getClientBusinessSource();
        String clientEnvSource = platformHttpHeadersDto.getClientEnvSource();
        Long accountId = platformAccountService.getJwtAccountId(request);
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(accountId)) {
            log.warn("未登录");
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "未登录！");
        }
        String umeng = platformAccountDeviceDto.getUmengDeviceId();
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(clientBusinessSource, umeng, clientEnvSource)) {
            log.error("请求参数不完整 accountId {} clientBusinessSource {} umeng {} clientEnvSource {}", accountId, clientBusinessSource, umeng, clientEnvSource);
            return Result.success();
        }
        if (Strings.isEmpty(clientBusinessSource) ||
                Strings.isEmpty(ip) ||
                Strings.isEmpty(umeng) ||
                Strings.isEmpty(clientEnvSource)) {
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不完整！");
        }
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(clientBusinessSource);
        if (null == platformSystem) {
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "参数异常！");
        }
        PlatformAccountDeviceUmeng platformAccountDeviceUmeng =
                platformAccountDeviceUmengDao.findByAccountId(accountId);
        Long time = System.currentTimeMillis();
        // 1、检查该用户是否绑定
        if (null == platformAccountDeviceUmeng) {
            // 没有绑定就检查 umeng是否绑定
            platformAccountDeviceUmeng = platformAccountDeviceUmengDao.findByDeviceId(umeng);
            // 用户和umeng未绑定直接新增。
            if (null == platformAccountDeviceUmeng) {
                platformAccountDeviceUmeng = PlatformAccountDeviceUmeng.builder().build();
                platformAccountDeviceUmeng.setSystemCode(clientBusinessSource);
                platformAccountDeviceUmeng.setDeviceId(umeng);
                platformAccountDeviceUmeng.setPlatformAccountId(accountId);
                platformAccountDeviceUmeng.setClientEnvSource(Integer.valueOf(clientEnvSource));
                platformAccountDeviceUmeng.setIsDel(false);
                platformAccountDeviceUmeng.setCreateTime(time);
                platformAccountDeviceUmeng.setUpdateTime(time);
                int res = platformAccountDeviceUmengDao.insertSelective(platformAccountDeviceUmeng);
                if (0 == res) {
                    return Result.error(Result.Code.NO_UPDATE.getCode(), "操作失败");
                }
                //  防止异常数据
                redisCacheService.del(platformAccountCacheKey.getAccountClientInfoKey(accountId));
                platformAccountDeviceManager.refreshAccountDevice(accountId);
            } else {
                // umeng被绑定则直接换帮用户信息
                Long oldAccountId = platformAccountDeviceUmeng.getPlatformAccountId();
                platformAccountDeviceUmeng.setSystemCode(clientBusinessSource);
                platformAccountDeviceUmeng.setClientEnvSource(Integer.valueOf(clientEnvSource));
                platformAccountDeviceUmeng.setPlatformAccountId(accountId);
                platformAccountDeviceUmeng.setIsDel(false);
                platformAccountDeviceUmeng.setUpdateTime(time);
                int res = platformAccountDeviceUmengDao.updateByPrimaryKeySelective(platformAccountDeviceUmeng);
                if (0 == res) {
                    return Result.error(Result.Code.NO_UPDATE.getCode(), "操作失败");
                }
                // 删除缓存信息
                redisCacheService.del(platformAccountCacheKey.getAccountClientInfoKey(accountId));
                redisCacheService.del(platformAccountCacheKey.getAccountClientInfoKey(oldAccountId));
                platformAccountDeviceManager.refreshAccountDevice(accountId);
                platformAccountDeviceManager.refreshAccountDevice(oldAccountId);
            }
        } else {
            // 2、用户已经绑定了。检查umeng是否被绑定
            PlatformAccountDeviceUmeng oldPlatformAccountDeviceUmeng = platformAccountDeviceUmengDao.findByDeviceId(umeng);
            if (null == oldPlatformAccountDeviceUmeng) {
                // ueng没有绑定直接更换用户绑定的umeng.
                platformAccountDeviceUmeng.setDeviceId(umeng);
                platformAccountDeviceUmeng.setUpdateTime(time);
                platformAccountDeviceUmeng.setSystemCode(clientBusinessSource);
                platformAccountDeviceUmeng.setClientEnvSource(Integer.valueOf(clientEnvSource));
                int res = platformAccountDeviceUmengDao.updateByPrimaryKeySelective(platformAccountDeviceUmeng);
                if (0 == res) {
                    return Result.error(Result.Code.NO_UPDATE.getCode(), "操作失败");
                }
                redisCacheService.del(platformAccountCacheKey.getAccountClientInfoKey(accountId));
                redisCacheService.del(platformAccountCacheKey.getAccountClientInfoKey(platformAccountDeviceUmeng.getPlatformAccountId()));
                platformAccountDeviceManager.refreshAccountDevice(accountId);
                platformAccountDeviceManager.refreshAccountDevice(platformAccountDeviceUmeng.getPlatformAccountId());
            } else {
                Long oldAccountId = oldPlatformAccountDeviceUmeng.getPlatformAccountId();
                // umeng已经被绑定了，直接修改umeng绑定的用户；并且清理当前用户的umengid
                oldPlatformAccountDeviceUmeng.setPlatformAccountId(accountId);
                oldPlatformAccountDeviceUmeng.setUpdateTime(time);
                oldPlatformAccountDeviceUmeng.setSystemCode(clientBusinessSource);
                int res = platformAccountDeviceUmengDao.updateByPrimaryKeySelective(oldPlatformAccountDeviceUmeng);
                if (0 == res) {
                    return Result.error(Result.Code.NO_UPDATE.getCode(), "操作失败");
                }
                platformAccountDeviceUmeng.setUpdateTime(time);
                platformAccountDeviceUmeng.setSystemCode(clientBusinessSource);
                platformAccountDeviceUmeng.setClientEnvSource(Integer.valueOf(clientEnvSource));
                platformAccountDeviceUmeng.setPlatformAccountId(0L);
                res = platformAccountDeviceUmengDao.updateByPrimaryKeySelective(platformAccountDeviceUmeng);
                if (0 == res) {
                    return Result.error(Result.Code.NO_UPDATE.getCode(), "操作失败");
                }
                redisCacheService.del(platformAccountCacheKey.getAccountClientInfoKey(accountId));
                redisCacheService.del(platformAccountCacheKey.getAccountClientInfoKey(oldAccountId));
                platformAccountDeviceManager.refreshAccountDevice(accountId);
                platformAccountDeviceManager.refreshAccountDevice(oldAccountId);
            }
        }
        return Result.success();
    }

}
