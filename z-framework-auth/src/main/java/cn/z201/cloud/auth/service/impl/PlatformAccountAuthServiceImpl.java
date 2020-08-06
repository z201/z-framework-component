package cn.z201.cloud.auth.service.impl;

import cn.z201.cloud.auth.config.AuthManagerContext;
import cn.z201.cloud.auth.config.SmsConfig;
import cn.z201.cloud.auth.dao.PlatformSystemOauthDao;
import cn.z201.cloud.auth.dto.*;
import cn.z201.cloud.auth.entity.PlatformAccount;
import cn.z201.cloud.auth.entity.PlatformAccountOauth;
import cn.z201.cloud.auth.entity.PlatformSystem;
import cn.z201.cloud.auth.entity.PlatformSystemOauth;
import cn.z201.cloud.auth.enums.PlatformAccountBindingInfoEnum;
import cn.z201.cloud.auth.enums.PlatformAccountOauthEnum;
import cn.z201.cloud.auth.enums.PlatformAccountSmsEnum;
import cn.z201.cloud.auth.exception.AuthException;
import cn.z201.cloud.auth.manager.*;
import cn.z201.cloud.auth.manager.dto.SMSParam;
import cn.z201.cloud.auth.manager.impl.PlatformAccountCacheKeyTools;
import cn.z201.cloud.auth.service.PlatformAccountServiceI;
import cn.z201.cloud.auth.service.PlatformHttpHeadersServiceI;
import cn.z201.cloud.auth.utils.*;
import cn.z201.cloud.auth.utils.dto.VerifyImageDto;
import com.alibaba.fastjson.JSONObject;
import cn.z201.cloud.auth.dao.PlatformAccountDao;
import cn.z201.cloud.auth.dao.PlatformAccountOauthDao;
import cn.z201.cloud.auth.service.PlatformAccountAuthServiceI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Service
public class PlatformAccountAuthServiceImpl implements PlatformAccountAuthServiceI {

    @Autowired
    PlatformAccountManagerI platformAccountManager;

    @Autowired
    PlatformAccountCacheManagerI platformAccountCacheManager;

    @Autowired
    PlatformAccountServiceI platformAccountService;

    @Autowired
    RedisCacheServiceI redisCacheService;

    @Autowired
    PlatformAccountCacheKeyTools platformAccountCacheKeyTools;

    @Autowired
    PlatformHttpHeadersServiceI platformHttpHeadersService;

    @Autowired
    PlatformAccountDeviceManagerI platformAccountDeviceManager;

    @Resource
    PlatformAccountOauthDao platformAccountOauthDao;

    @Resource
    PlatformAccountDao platformAccountDao;

    /**
     * 临时代码,这里需要单独走缓存
     */
    @Resource
    PlatformSystemOauthDao platformSystemOauthDao;

    @Resource
    AuthManagerContext authManagerContext;

    @Override
    public Object accountBindingInfo(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        Long accountId = platformAccountService.getJwtAccountId(request);
        PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
        if (null == platformAccount) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "账号不存在");
        }
        ArrayList<PlatformAccountBindingInfoDto> platformAccountBindingInfoList = new ArrayList<>();
        PlatformAccountBindingInfoDto temp = null;
        String phone = platformAccount.getPhoneNumber();
        if (Strings.isEmpty(phone)) {
            temp = createByPlatformAccountBindingInfoEnumAndStatusAndBindingStatus(
                    PlatformAccountBindingInfoEnum.PHONE,
                    true, false, "未绑定");
        } else {
            temp = createByPlatformAccountBindingInfoEnumAndStatusAndBindingStatus(
                    PlatformAccountBindingInfoEnum.PHONE,
                    true, true, phone);
        }
        platformAccountBindingInfoList.add(temp);
        String pwd = platformAccount.getPassword();
        if (Strings.isEmpty(pwd)) {
            temp = createByPlatformAccountBindingInfoEnumAndStatusAndBindingStatus(
                    PlatformAccountBindingInfoEnum.PWD,
                    true, false, "未设置");
        } else {
            temp = createByPlatformAccountBindingInfoEnumAndStatusAndBindingStatus(
                    PlatformAccountBindingInfoEnum.PWD,
                    true, true, "已经设置");
        }
        platformAccountBindingInfoList.add(temp);
        // 这里需要额外处理
        List<PlatformAccountOauth> platformAccountOauthList = platformAccountOauthDao.listByAccountId(accountId);
        if (null != platformAccountOauthList && !platformAccountOauthList.isEmpty()) {
            for (PlatformAccountOauth platformAccountOauth : platformAccountOauthList) {
                // TODO 现在只有微信那就这样吧
                if (platformAccountOauth.getOauthCode().equals(PlatformAccountBindingInfoEnum.WX.getCode())) {
                    temp = createByPlatformAccountBindingInfoEnumAndStatusAndBindingStatus(
                            PlatformAccountBindingInfoEnum.WX,
                            true, true, "已经设置");
                    platformAccountBindingInfoList.add(temp);
                }
            }
        } else {
            temp = createByPlatformAccountBindingInfoEnumAndStatusAndBindingStatus(
                    PlatformAccountBindingInfoEnum.WX,
                    true, false, "未设置");
            platformAccountBindingInfoList.add(temp);
        }
        return Result.success(AccountBindingInfoDto.builder().accountBindingInfoList(platformAccountBindingInfoList).build());
    }

    @Override
    public Object accountOauth(PlatformAccountDto platformAccountDto,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        Integer type = platformAccountDto.getType();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        PlatformSystemOauth platformSystemOauth = platformSystemOauthDao.findBySystemCodeAndOauthType(code, PlatformAccountOauthEnum.APP_WE_CHAT.getCode());
        AuthTokenDto authTokenDto = authManagerContext.getInstance(PlatformAccountOauthEnum.APP_WE_CHAT.getCode())
                .oAuth(platformAccountDto, platformSystemOauth);
        PlatformAccountOauth platformAccountOauth = null;
        Long accountId = 0L;
        // 获取账号唯一标识下所有的授权信息
        List<PlatformAccountOauth> platformAccountOauthList =
                platformAccountOauthDao.listByOauthIdAndSystemCode(authTokenDto.getUnionId(), platformSystem.getAccountLibraryCode());
        PlatformAccount platformAccount = null;
        // 检查账号是否未注册过,未注册过则直接检查是否绑定了相关的第三方信息，绑定了则直接登录
        if (null != platformAccountOauthList && !platformAccountOauthList.isEmpty()) {
            for (PlatformAccountOauth accountOauth : platformAccountOauthList) {
                if (accountOauth.getOauthCode().equals(PlatformAccountOauthEnum.APP_WE_CHAT.getCode())) {
                    platformAccountOauth = platformAccountOauthDao.findByOauthIdAndSystemCodeAndOauthCode(authTokenDto.getUnionId(),
                            platformSystem.getAccountLibraryCode(), PlatformAccountOauthEnum.APP_WE_CHAT.getCode());
                }
                if (0 == accountId) {
                    if (accountOauth.getIsEffective().equals(1)) {
                        accountId = accountOauth.getAccountId();
                    }
                }
            }
        } else {
            // 第一次注册
            platformAccount = platformAccountManager.registeredByOauth(authTokenDto, platformSystem, platformSystemOauth, ip);
            accountId = platformAccount.getId();
            platformAccountDeviceManager.refreshAccountDevice(accountId);
        }
        if (null == platformAccount) {
            if (null == platformAccountOauth) {
                // 如果存在第三方关联账号，则直接绑定注册第三方。
                if (0 != accountId) {
                    platformAccountManager.accountBindingOauth(accountId, PlatformAccountOauthEnum.APP_WE_CHAT.getCode(), platformSystem, authTokenDto);
                } else {
                    // 直接注册
                    platformAccount = platformAccountManager.registeredByOauth(authTokenDto, platformSystem, platformSystemOauth, ip);
                    accountId = platformAccount.getId();
                }
                platformAccountDeviceManager.refreshAccountDevice(accountId);
            } else {
                if (platformAccountOauth.getAccountId() == 0 && accountId == 0) {
                    // 第三方不写入，只更新
                    platformAccount = platformAccountManager.registeredByOauth(platformAccountOauth, platformSystem, platformSystemOauth, ip);
                    accountId = platformAccount.getId();
                    platformAccountDeviceManager.refreshAccountDevice(accountId);
                }
                // 如果第三方被解绑.直接绑定
                if (platformAccountOauth.getIsEffective() == 0) {
                    Long time = System.currentTimeMillis();
                    platformAccountOauth.setAccountId(accountId);
                    platformAccountOauth.setIsEffective(1);
                    platformAccountOauth.setUpdateTime(time);
                    platformAccountOauthDao.updateByPrimaryKeySelective(platformAccountOauth);
                    platformAccountDeviceManager.refreshAccountDevice(accountId);
                }
            }
        }
        return loginSuccess(accountId, platformSystem.getAccountLibraryCode(), authTokenDto.getUserInfoDto());
    }

    @Override
    public Object accountMiniProgramOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        PlatformSystemOauth platformSystemOauth = platformSystemOauthDao.findBySystemCodeAndOauthType(code, PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode());
        AuthTokenDto authTokenDto = authManagerContext.getInstance(PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode()).oAuth(platformAccountDto, platformSystemOauth);
        // 这里需要额外处理
        PlatformAccountOauth platformAccountOauth = platformAccountOauthDao.findByOauthIdAndSystemCodeAndOauthCode(authTokenDto.getUnionId(),
                platformSystem.getAccountLibraryCode(), PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode());
        if (null == platformAccountOauth || platformAccountOauth.getIsEffective() == 0) {
            return noRegistered(code, authTokenDto.getOpenId(), authTokenDto.getSessionKey());
        }
        AuthUserInfoDto authUserInfoDto = null;
        if (null != authTokenDto.getUserInfoDto()) {
            authUserInfoDto = authTokenDto.getUserInfoDto();
        } else {
            authUserInfoDto = AuthUserInfoDto.builder().build();
            authUserInfoDto.setSourceType(3);
        }
        platformAccountDeviceManager.refreshAccountDevice(platformAccountOauth.getAccountId());
        return loginSuccess(platformAccountOauth.getAccountId(), code, authTokenDto.getOpenId(), authUserInfoDto);
    }

    /**
     * TODO 微信公众号授权 这个代码要重构
     *
     * @param platformAccountDto
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object accountWapOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String clientBusinessSource = platformHttpHeadersDto.getClientBusinessSource();
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        PlatformSystemOauth platformSystemOauth = platformSystemOauthDao.findBySystemCodeAndOauthType(code, PlatformAccountOauthEnum.WAP_WE_CHAT.getCode());
        AuthTokenDto authTokenDto = authManagerContext.getInstance(PlatformAccountOauthEnum.WAP_WE_CHAT.getCode()).oAuth(platformAccountDto, platformSystemOauth);
        PlatformAccountOauth platformAccountOauth = null;
        PlatformAccount platformAccount = null;
        Long accountId = 0L;
        // 获取账号唯一标识下所有的授权信息
        List<PlatformAccountOauth> platformAccountOauthList =
                platformAccountOauthDao.listByOauthIdAndSystemCode(authTokenDto.getUnionId(), platformSystem.getAccountLibraryCode());
        // 检查账号是否未注册过,未注册过则直接检查是否绑定了相关的第三方信息，绑定了则直接登录
        if (null != platformAccountOauthList && !platformAccountOauthList.isEmpty()) {
            for (PlatformAccountOauth accountOauth : platformAccountOauthList) {
                if (accountOauth.getOauthCode().equals(PlatformAccountOauthEnum.WAP_WE_CHAT.getCode())) {
                    platformAccountOauth = platformAccountOauthDao.findByOauthIdAndSystemCodeAndOauthCode(authTokenDto.getUnionId(),
                            platformSystem.getAccountLibraryCode(), PlatformAccountOauthEnum.WAP_WE_CHAT.getCode());
                }
                if (0 == accountId) {
                    if (accountOauth.getIsEffective().equals(1)) {
                        accountId = accountOauth.getAccountId();
                    }
                }
            }
        } else {
            // 第一次注册
            platformAccount = platformAccountManager.registeredByOauth(authTokenDto, platformSystem, platformSystemOauth, ip);
            accountId = platformAccount.getId();
            platformAccountDeviceManager.refreshAccountDevice(accountId);
        }
        if (null == platformAccount) {
            if (null == platformAccountOauth) {
                // 如果存在第三方关联账号，则直接绑定注册第三方。
                if (0 != accountId) {
                    platformAccountManager.accountBindingOauth(accountId, PlatformAccountOauthEnum.WAP_WE_CHAT.getCode(), platformSystem, authTokenDto);
                } else {
                    // 直接注册
                    platformAccount = platformAccountManager.registeredByOauth(authTokenDto, platformSystem, platformSystemOauth, ip);
                    accountId = platformAccount.getId();
                }
                platformAccountDeviceManager.refreshAccountDevice(accountId);
            } else {
                if (platformAccountOauth.getAccountId() == 0 && accountId == 0) {
                    // 第三方不写入，只更新
                    platformAccount = platformAccountManager.registeredByOauth(platformAccountOauth, platformSystem, platformSystemOauth, ip);
                    accountId = platformAccount.getId();
                    platformAccountDeviceManager.refreshAccountDevice(accountId);
                }
                // 如果第三方被解绑.直接绑定
                if (platformAccountOauth.getIsEffective() == 0) {
                    Long time = System.currentTimeMillis();
                    platformAccountOauth.setAccountId(accountId);
                    platformAccountOauth.setIsEffective(1);
                    platformAccountOauth.setUpdateTime(time);
                    platformAccountOauthDao.updateByPrimaryKeySelective(platformAccountOauth);
                    platformAccountDeviceManager.refreshAccountDevice(accountId);
                }
            }
        }
        return loginWapSuccess(accountId, platformSystem.getCode(), authTokenDto.getUserInfoDto());
    }

    private Object noRegistered(String systemCode, String openId, String sessionKey) {
        Map<String, Object> map = new HashMap<>();
        String key = platformAccountCacheKeyTools.getAccountSessionKey(systemCode, openId);
        if (!redisCacheService.setStr(key, sessionKey, PlatformAccountCacheKey.EXPIRATION)) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "授权失败,请稍后尝试");
        }
        map.put("openId", openId);
        map.put("code", "");
        map.put("userState", false);
        return Result.success(map);
    }

    @Override
    public Object accountMiniProgramRegistered(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String openId = platformAccountDto.getOpenId();
        String encryptedData = platformAccountDto.getEncryptedData();
        String ivParameter = platformAccountDto.getIv();
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(openId, encryptedData, ivParameter)) {
            log.warn("请求参数不合法，请将参数填写完整");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不完整，请将参数填写完整。");
        }
        String key = platformAccountCacheKeyTools.getAccountSessionKey(code, openId);
        String sessionKey = redisCacheService.getStr(key);
        if (null == sessionKey) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "注册失败,请重新尝试", "微信注册失败,sessionKey 获取失败");
        }
        String decryptData = WXMiniAESOperator.decrypt(encryptedData, sessionKey, ivParameter);
        if (StringUtils.isEmpty(decryptData)) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "注册失败,请重新尝试", "微信注册失败,decryptData 获取失败 openId {}");
        }
        JSONObject decryptDataJson = JSONObject.parseObject(decryptData);
        if (!decryptDataJson.containsKey("unionId")) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "注册失败,请重新尝试", "微信注册失败,unionId 获取失败 openid {}");
        }
        PlatformSystemOauth platformSystemOauth = platformSystemOauthDao.findBySystemCodeAndOauthType(code, PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode());
        String unionId = decryptDataJson.getString("unionId");
        String userName = decryptDataJson.getString("nickName");
        String userHead = decryptDataJson.getString("avatarUrl").replaceAll("/132|/46|/64|/96", "/0");
        Integer userSex = decryptDataJson.getIntValue("gender");
        String gender = "";
        if (1 == userSex) {
            gender = "男";
        }
        if (2 == userSex) {
            gender = "女";
        }
        AuthUserInfoDto authUserInfoDto = AuthUserInfoDto.builder()
                .nickname(userName)
                .avatar(userHead)
                .gender(gender)
                .sourceType(3)
                .build();
        // 这里需要额外处理
        AuthTokenDto authTokenDto = AuthTokenDto.builder()
                .openId(openId)
                .unionId(unionId)
                .userInfoDto(authUserInfoDto)
                .build();
        PlatformAccountOauth platformAccountOauth = null;
        PlatformAccount platformAccount = null;
        Long accountId = 0L;
        // 获取账号唯一标识下所有的授权信息
        List<PlatformAccountOauth> platformAccountOauthList =
                platformAccountOauthDao.listByOauthIdAndSystemCode(authTokenDto.getUnionId(), platformSystem.getAccountLibraryCode());
        // 检查账号是否未注册过,未注册过则直接检查是否绑定了相关的第三方信息，绑定了则直接登录
        if (null != platformAccountOauthList && !platformAccountOauthList.isEmpty()) {
            // 存在相关第三方选第三放信息。
            for (PlatformAccountOauth accountOauth : platformAccountOauthList) {
                if (accountOauth.getOauthCode().equals(PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode())) {
                    platformAccountOauth = platformAccountOauthDao.findByOauthIdAndSystemCodeAndOauthCode(authTokenDto.getUnionId(),
                            platformSystem.getAccountLibraryCode(), PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode());
                }
                if (accountOauth.getIsEffective().equals(1)) {
                    accountId = accountOauth.getAccountId();
                }
            }
        } else {
            // 第一次注册
            platformAccount = platformAccountManager.registeredByOauth(authTokenDto, platformSystem, platformSystemOauth, ip);
            accountId = platformAccount.getId();
        }
        if (null == platformAccount) {
            if (null == platformAccountOauth) {
                // 如果存在第三方关联账号，则直接绑定注册第三方。
                if (0 != accountId) {
                    platformAccountManager.accountBindingOauth(accountId, PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode(), platformSystem, authTokenDto);
                } else {
                    // 直接注册
                    platformAccount = platformAccountManager.registeredByOauth(authTokenDto, platformSystem, platformSystemOauth, ip);
                    accountId = platformAccount.getId();
                }
                platformAccountDeviceManager.refreshAccountDevice(accountId);
            } else {
                if (platformAccountOauth.getAccountId() == 0 && accountId == 0) {
                    // 第三方不写入，只更新
                    platformAccount = platformAccountManager.registeredByOauth(platformAccountOauth, platformSystem, platformSystemOauth, ip);
                    accountId = platformAccount.getId();
                    platformAccountDeviceManager.refreshAccountDevice(accountId);
                }
                // 如果第三方被解绑.直接绑定
                if (platformAccountOauth.getIsEffective() == 0) {
                    Long time = System.currentTimeMillis();
                    platformAccountOauth.setAccountId(accountId);
                    platformAccountOauth.setIsEffective(1);
                    platformAccountOauth.setUpdateTime(time);
                    platformAccountOauth.setAccountId(accountId);
                    platformAccountOauthDao.updateByPrimaryKeySelective(platformAccountOauth);
                    platformAccountDeviceManager.refreshAccountDevice(accountId);
                }
            }
        }
        String token = createCodeLoginSuccess(accountId, platformSystem.getCode(), authUserInfoDto);
        Map<String, Object> map = new HashMap<>();
        map.put("code", token);
        map.put("userState", true);
        map.put("openId", openId);
        return Result.success(map);
    }


    @Override
    public Object accountMiniProgramPhoneBinding(PlatformAccountDto platformAccountDto,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {
        Long accountId = platformAccountService.getJwtAccountId(request);
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String openId = platformAccountDto.getOpenId();
        String encryptedData = platformAccountDto.getEncryptedData();
        String ivParameter = platformAccountDto.getIv();
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(openId, encryptedData, ivParameter)) {
            log.warn("请求参数不合法，请将参数填写完整");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不完整，请将参数填写完整。");
        }
        String key = platformAccountCacheKeyTools.getAccountSessionKey(code, openId);
        String sessionKey = redisCacheService.getStr(key);
        if (null == sessionKey) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "绑定手机号失败,重新尝试", "绑定手机号失败,sessionKey 获取失败");
        }
        String decryptData = WXMiniAESOperator.decrypt(encryptedData, sessionKey, ivParameter);
        if (StringUtils.isEmpty(decryptData)) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "绑定手机号失败,重新尝试", "绑定手机号失败,decryptData 获取失败 openId {}");
        }
        JSONObject decryptDataJson = JSONObject.parseObject(decryptData);
        if (!decryptDataJson.containsKey("phoneNumber")) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "绑定手机号失败,未获取到该微信的手机号信息", "绑定手机号失败,unionId 获取失败 openid {}");
        }
        String phone = decryptDataJson.getString("phoneNumber");
        PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
        if (null == platformAccount) {
            log.warn("数据错乱,该账号不存");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "数据错乱,该账号不存");
        }
        String str = platformAccountDao.existsPhone(phone, platformSystem.getAccountLibraryCode());
        if (null != str) {
            log.warn("请求参数不合法，新手机号已经被使用了");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "新手机号已经被使用了。");
        }
        String oldPhone = platformAccount.getPhoneNumber();
        if (null != oldPhone) {
            oldPhone = oldPhone.trim();
            if (!Strings.isEmpty(oldPhone)) {
                log.warn("该账号已经绑定手机号，请换手机号");
                throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "该账号已经绑定手机号,请换手机号。");
            }
        }
        platformAccount.setPhoneNumber(phone);
        int res = platformAccountDao.updateByPrimaryKeySelective(platformAccount);
        if (res == 0) {
            log.error("手机号更新失败 phone {} ", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "手机号更新失败，请稍后尝试！");
        }
        platformAccountCacheManager.refreshPlatformAccountCache(platformAccount);
        Map<String, String> map = new HashMap<>();
        map.put("newPhone", phone);
        return Result.success(map);
    }

    @Override
    public Object accountMiniProgramPhoneReadyPhoneBinding(PlatformAccountDto platformAccountDto,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {
        Long accountId = platformAccountService.getJwtAccountId(request);
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        PlatformSystemOauth platformSystemOauth = platformSystemOauthDao.findBySystemCodeAndOauthType(code, PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode());
        AuthTokenDto authTokenDto = authManagerContext.getInstance(PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode()).oAuth(platformAccountDto, platformSystemOauth);
        String key = platformAccountCacheKeyTools.getAccountSessionKey(code, authTokenDto.getOpenId());
        redisCacheService.del(key);
        if (!redisCacheService.setStr(key, authTokenDto.getSessionKey(), 72000000L)) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "授权失败,请稍后尝试");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("openId", authTokenDto.getOpenId());
        return Result.success(map);
    }

    @Override
    public Object accountBindingOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        Long accountId = platformAccountService.getJwtAccountId(request);
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String smsCode = platformAccountDto.getSmsCode();
        Integer type = platformAccountDto.getType();
        PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
        if (null == platformAccount) {
            log.error("数据异常,该账号未注册 accountId {} ", accountId);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "数据异常,该账号未注册");
        }
        String phone = platformAccount.getPhoneNumber();
        if (Strings.isEmpty(phone)) {
            log.error("数据异常,该账号未注册 accountId {} ", accountId);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "请绑定手机号后,在绑定第三方");
        }
        PlatformAccountOauthEnum platformAccountOauthEnum = PlatformAccountOauthEnum.findPlatformAccountOauthEnumByCode(type);
        if (null == platformAccountOauthEnum) {

        }
        String key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), phone, PlatformAccountSmsEnum.CHECK_PHONE.getCode());
        String smsCodeCache = redisCacheService.getStr(key);
        ParamCheckUtil.checkAccountSmsCodeAfterThrowException(smsCode, smsCodeCache);
        PlatformSystemOauth platformSystemOauth = platformSystemOauthDao.findBySystemCodeAndOauthType(code, platformAccountOauthEnum.getCode());
        AuthTokenDto authTokenDto = authManagerContext.getInstance(platformAccountOauthEnum.getCode()).oAuth(platformAccountDto, platformSystemOauth);
        // 这里需要额外处理
        PlatformAccountOauth platformAccountOauth = platformAccountOauthDao.findByOauthIdAndSystemCodeAndOauthCode(authTokenDto.getUnionId(),
                platformSystem.getAccountLibraryCode(),
                PlatformAccountOauthEnum.APP_WE_CHAT.getCode());
        if (null == platformAccountOauth) {
            platformAccountManager.accountBindingOauth(accountId, PlatformAccountOauthEnum.APP_WE_CHAT.getCode(), platformSystem, authTokenDto);
        } else {
            if (platformAccountOauth.getIsEffective() == 1) {
                if (!platformAccountOauth.getAccountId().equals(accountId)) {
                    log.error("第三方绑定失败 accountId {} ", accountId);
                    throw new AuthException(Result.Code.NO_UPDATE.getCode(), "第三方绑定失败,该账号已经被绑定！");
                }
            } else {
                platformAccountOauth.setIsEffective(1);
                platformAccountOauth.setAccountId(accountId);
                int res = platformAccountOauthDao.updateByPrimaryKeySelective(platformAccountOauth);
                if (res == 0) {
                    log.error("第三方绑定失败 accountId {} ", accountId);
                    throw new AuthException(Result.Code.NO_UPDATE.getCode(), "第三方绑定失败，请重新尝试！");
                }
            }
        }
        platformAccountDeviceManager.refreshAccountDevice(accountId);
        return Result.success();
    }

    @Override
    public Object accountUntiedOauth(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse response) {
        Long accountId = platformAccountService.getJwtAccountId(request);
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        Integer type = platformAccountDto.getType();
        // 这里需要额外处理
        List<PlatformAccountOauth> platformAccountOauthList = platformAccountOauthDao.listByAccountIdAndSystemCode(accountId, platformSystem.getAccountLibraryCode());
        if (null == platformAccountOauthList || platformAccountOauthList.isEmpty()) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "解除绑定失败,第三方未被绑定！");
        }
        int res = platformAccountOauthDao.updateEffectiveAccountIdAndSystemCode(accountId, platformSystem.getAccountLibraryCode(), 0);
        if (res == 0) {
            log.error("解除绑定失败 accountId {} ", accountId);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "解除绑定失败，请重新尝试！");
        }
        platformAccountDeviceManager.refreshAccountDevice(accountId);
        String cacheKey = PlatformAccountOnlineCacheKeyTools.getAccountOauthUserInfoKey(platformSystem.getCode(), accountId);
        redisCacheService.del(cacheKey);
        return Result.success();
    }

    @Autowired
    SmsConfig smsConfig;

    @Override
    public Object phoneSendSms(PlatformAccountDto platformAccountDto, HttpServletRequest
            request, HttpServletResponse response) {
        String phone = platformAccountDto.getPhone();
        Integer smsType = platformAccountDto.getType();
        ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZeroAfterThrowException(phone, smsType);
        ParamCheckUtil.checkPhoneAfterThrowException(phone);
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        // 这里需要做大规模的校验
        String key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), phone, smsType);
        String smsCode = RandomUtil.getRandomLetter(4, RandomUtil.CodeType.ALL_NUMBER);
        // 过期时间不对
        if (!redisCacheService.setStr(key, smsCode, PlatformAccountCacheKey.EXPIRATION)) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "短信发送失败");
        }
        SMSParam smsParam = new SMSParam();
        smsParam.setMobile(phone);
        smsParam.setContent(smsConfig.getName() + " 验证码为:" + smsCode + "(5分钟有效)，您正在使用手机号登录。若非本人操作，请忽略本短信");
        smsParam.setCode(smsCode);
        return Result.success();
    }

    @Override
    public Object phoneCheckSms(PlatformAccountDto platformAccountDto, HttpServletRequest
            request, HttpServletResponse response) {
        String phone = platformAccountDto.getPhone();
        String smsCode = platformAccountDto.getSmsCode();
        Integer smsType = platformAccountDto.getType();
        ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZeroAfterThrowException(phone, smsCode);
        ParamCheckUtil.checkPhoneAfterThrowException(phone);
        PlatformAccountSmsEnum.findAndCheckCodeAfterThrowException(smsType);
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), phone, smsType);
        String smsCodeCache = redisCacheService.getStr(key);
        ParamCheckUtil.checkAccountSmsCodeAfterThrowException(smsCode, smsCodeCache);
        if (!smsCodeCache.equals(smsCode)) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "短信错误");
        }
        return Result.success();
    }


    @Override
    public Object phoneSmsLogin(PlatformAccountDto platformAccountDto, HttpServletRequest
            request, HttpServletResponse response) {
        String phone = platformAccountDto.getPhone();
        String smsCode = platformAccountDto.getSmsCode();
        String channel = platformAccountDto.getChannel();
        ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZeroAfterThrowException(phone, smsCode);
        ParamCheckUtil.checkPhoneAfterThrowException(phone);
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), phone, PlatformAccountSmsEnum.LOGIN.getCode());
        String smsCodeCache = redisCacheService.getStr(key);
        ParamCheckUtil.checkAccountSmsCodeAfterThrowException(smsCode, smsCodeCache);
        PlatformAccount platformAccount = platformAccountDao.findByPhoneAndSystemCode(phone, platformSystem.getAccountLibraryCode());
        if (null == platformAccount) {
            // TODO 这里需要区分是 手机号注册、第三方注册、邮箱注册。同时还有区分注册的业务来源。
            platformAccount = platformAccountManager.registeredByPhone(platformSystem, phone, channel, ip);

        }
        AuthUserInfoDto authUserInfoDto = AuthUserInfoDto.builder()
                .phone(phone)
                .sourceType(1)
                .build();
        return loginSuccess(platformAccount.getId(), platformSystem.getAccountLibraryCode(), authUserInfoDto);
    }

    @Override
    public Object phonePasswordLogin(PlatformAccountDto platformAccountDto, HttpServletRequest
            request, HttpServletResponse response) {
        String phone = platformAccountDto.getPhone();
        String pwd = platformAccountDto.getPwd();
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZeroAfterThrowException(phone, pwd);
        ParamCheckUtil.checkPhoneAfterThrowException(phone);
        PlatformAccount platformAccount = platformAccountDao.findByPhoneAndSystemCode(phone, platformSystem.getAccountLibraryCode());
        if (null == platformAccount) {
            log.error("账号不存在 phone {} 未注册", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "账号或密码填写错误!");
        }
        if (platformAccount.getIsEffective() != 1) {
            log.error("账号不可用 phone {} 账号被停用", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "账号被停用,请联系客户人员!");
        }
        if (!platformAccount.getPassword().equals(EncryptPassword.encrypt(pwd))) {
            log.error("密码填写错误 phone {} ", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "账号或密码填写错误!");
        }
        AuthUserInfoDto authUserInfoDto = AuthUserInfoDto.builder()
                .phone(phone)
                .sourceType(1)
                .build();
        return loginSuccess(platformAccount.getId(), platformSystem.getAccountLibraryCode(), authUserInfoDto);
    }

    @Override
    public Object smsCodePasswordForget(PlatformAccountDto platformAccountDto, HttpServletRequest
            request, HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        String phone = platformAccountDto.getPhone();
        String smsCode = platformAccountDto.getSmsCode();
        String pwd = platformAccountDto.getPwd();
        String pwd2 = platformAccountDto.getPwd2();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZeroAfterThrowException(phone, smsCode, pwd, pwd2);
        ParamCheckUtil.checkPhoneAfterThrowException(phone);
        if (pwd.length() < 6 || pwd.length() > 15) {
            log.warn("请输入合法的密码。密码长度6-16位。");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请输入合法的密码。密码长度6-16位。");
        }
        if (!pwd.equals(pwd2)) {
            log.warn("两次密码输入不一致。");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "两次密码输入不一致");
        }
        String key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), phone, PlatformAccountSmsEnum.FORGET.getCode());
        String smsCodeCache = redisCacheService.getStr(key);
        ParamCheckUtil.checkAccountSmsCodeAfterThrowException(smsCode, smsCodeCache);
        PlatformAccount platformAccount = platformAccountDao.findByPhoneAndSystemCode(phone, platformSystem.getAccountLibraryCode());
        if (null == platformAccount) {
            log.error("找回失败,该账号未注册 phone {} ", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "您输入的手机号（账号）不存在！");
        }
        platformAccount.setPassword(EncryptPassword.encrypt(pwd));
        int res = platformAccountDao.updateByPrimaryKeySelective(platformAccount);
        if (res == 0) {
            log.error("密码修改失败 phone {} ", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "密码修改失败，请稍后尝试！");
        }
        return Result.success();
    }

    @Override
    public Object changePassword(PlatformAccountDto platformAccountDto, HttpServletRequest
            request, HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        Long accountId = platformAccountService.getJwtAccountId(request);
        String smsCode = platformAccountDto.getSmsCode();
        String pwd = platformAccountDto.getPwd();
        String pwd2 = platformAccountDto.getPwd2();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZeroAfterThrowException(smsCode, pwd, pwd2);
        PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
        if (null == platformAccount) {
            log.warn("数据错乱,该账号不存");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "数据错乱,该账号不存");
        }
        if (pwd.length() < 6 || pwd.length() > 15) {
            log.warn("请输入合法的密码。密码长度6-16位。");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请输入合法的密码。密码长度6-16位。");
        }
        if (!pwd.equals(pwd2)) {
            log.warn("两次密码输入不一致。");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "两次密码输入不一致");
        }
        String phone = platformAccount.getPhoneNumber();
        ParamCheckUtil.checkPhoneAfterThrowException(phone);
        String key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), phone, PlatformAccountSmsEnum.RESET_PWD.getCode());
        String smsCodeCache = redisCacheService.getStr(key);
        ParamCheckUtil.checkAccountSmsCodeAfterThrowException(smsCode, smsCodeCache);
        platformAccount.setPassword(EncryptPassword.encrypt(pwd));
        int res = platformAccountDao.updateByPrimaryKeySelective(platformAccount);
        if (res == 0) {
            log.error("密码修改失败 phone {} ", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "密码修改失败，请稍后尝试！");
        }
        return Result.success();
    }

    @Override
    public Object changeBindingPhone(PlatformAccountDto platformAccountDto, HttpServletRequest
            request, HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        Long accountId = platformAccountService.getJwtAccountId(request);
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String smsCode = platformAccountDto.getSmsCode();
        String newPhone = platformAccountDto.getNewPhone();
        String newPhoneSmsCode = platformAccountDto.getNewPhoneSmsCode();
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(smsCode, newPhone, newPhoneSmsCode)) {
            log.warn("请求参数不合法，请将参数填写完整");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不完整，请将参数填写完整。");
        }
        PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
        if (null == platformAccount) {
            log.warn("数据错乱,该账号不存");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "数据错乱,该账号不存");
        }
        String str = platformAccountDao.existsPhone(newPhone, platformSystem.getAccountLibraryCode());
        if (null != str) {
            log.warn("请求参数不合法，新手机号已经被使用了");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "新手机号已经被使用了。");
        }
        String phone = platformAccount.getPhoneNumber();
        String key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), phone, PlatformAccountSmsEnum.CHECK_PHONE.getCode());
        String smsCodeCache = redisCacheService.getStr(key);
        if (smsCodeCache == null || !smsCodeCache.equals(smsCode)) {
            log.warn("短信验证码错误，请检查输入验证码");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "短信验证码错误，请检查输入短信验证码");
        }
        key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), newPhone, PlatformAccountSmsEnum.CHECK_PHONE.getCode());
        String newPhoneSmsCodeCache = redisCacheService.getStr(key);
        if (newPhoneSmsCodeCache == null || !newPhoneSmsCodeCache.equals(newPhoneSmsCode)) {
            log.warn("短信验证码错误，请检查输入验证码");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "短信验证码错误，请检查输入短信验证码");
        }
        platformAccount.setPhoneNumber(newPhone);
        int res = platformAccountDao.updateByPrimaryKeySelective(platformAccount);
        if (res == 0) {
            log.error("手机号更新失败 phone {} ", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "手机号更新失败，请稍后尝试！");
        }
        platformAccountCacheManager.refreshPlatformAccountCache(platformAccount);
        Map<String, String> map = new HashMap<>();
        map.put("newPhone", newPhone);
        return Result.success(map);
    }

    @Override
    public Object bindingPhone(PlatformAccountDto platformAccountDto, HttpServletRequest
            request, HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        Long accountId = platformAccountService.getJwtAccountId(request);
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String phone = platformAccountDto.getPhone();
        String smsCode = platformAccountDto.getSmsCode();
        PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
        if (null == platformAccount) {
            log.warn("数据错乱,该账号不存");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "数据错乱,该账号不存");
        }
        String str = platformAccountDao.existsPhone(phone, platformSystem.getAccountLibraryCode());
        if (null != str) {
            log.warn("请求参数不合法，新手机号已经被使用了");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "新手机号已经被使用了。");
        }
        String key = platformAccountCacheKeyTools.getAccountSmsCodeCacheKey(platformSystem.getCode(), phone, PlatformAccountSmsEnum.CHECK_PHONE.getCode());
        String smsCodeCache = redisCacheService.getStr(key);
        if (smsCodeCache == null || !smsCodeCache.equals(smsCode)) {
            log.warn("短信验证码错误，请检查输入验证码");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "短信验证码错误，请检查输入短信验证码");
        }
        String oldPhone = platformAccount.getPhoneNumber();
        if (null != oldPhone) {
            oldPhone = oldPhone.trim();
            if (!Strings.isEmpty(oldPhone)) {
                log.warn("该账号已经绑定手机号，请换手机号");
                throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "该账号已经绑定手机号,请换手机号。");
            }
        }
        platformAccount.setPhoneNumber(phone);
        int res = platformAccountDao.updateByPrimaryKeySelective(platformAccount);
        if (res == 0) {
            log.error("手机号更新失败 phone {} ", phone);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "手机号更新失败，请稍后尝试！");
        }
        platformAccountCacheManager.refreshPlatformAccountCache(platformAccount);
        Map<String, String> map = new HashMap<>();
        map.put("newPhone", phone);
        return Result.success(map);
    }

    @Override
    public Object checkPhone(PlatformAccountDto platformAccountDto, HttpServletRequest request, HttpServletResponse
            response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        String phone = platformAccountDto.getPhone();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        PlatformAccount platformAccount = platformAccountDao.findByPhoneAndSystemCode(phone, platformSystem.getAccountLibraryCode());
        String state = "0";
        if (null == platformAccount) {
            log.info("账号不存在 phone {} 未注册", phone);
            state = "1";
        } else if (platformAccount.getIsEffective() != 1) {
            log.info("账号被停用 phone {}", phone);
            state = "2";
        }
        Map<String, String> map = new HashMap<>();
        map.put("state", state);
        return Result.success(map);
    }

    @Override
    public void verifyImage(HttpServletRequest request, HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String imageId = UUID.randomUUID().toString();
        String key = platformAccountCacheKeyTools.getAccountImageId(platformSystem.getCode(), imageId);
        VerifyImageDto verifyImageDto = VerifyUtil.arithmeticCaptcha();
        if (!redisCacheService.setStr(key, verifyImageDto.getCode(), VerifyUtil.EXPIRATION)) {
            log.warn("生成图形验证码失败");
            throw new AuthException(Result.Code.ERROR.getCode(), "生成图形验证码失败,请重新尝试。");
        }
        // 设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        //设置HttpOnly属性,防止Xss攻击
        response.setHeader(HttpHeaders.SET_COOKIE, "name=value; HttpOnly");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
        // 设置响应头信息，告诉浏览器不要缓存此内容
        response.setDateHeader("Expire", VerifyUtil.EXPIRATION);
        response.setHeader("Image-Id", imageId);
        try {
            Integer contentLength = verifyImageDto.getBos().size();
            response.setHeader("content-length", contentLength + "");
            // 将内存中的图片通过流动形式输出到客户端
            response.getOutputStream().write(verifyImageDto.getBos().toByteArray());
        } catch (Exception e) {
            log.warn("生成图形验证码失败 {} ", e.getMessage());
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "图形验证码获取失败,请稍后尝试！");
        } finally {
            try {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (Exception e2) {
                log.warn("图形写入响应流失败 {} ", e2.getMessage());
            }
        }
    }

    @Override
    public Object verifyImageStatus(PlatformAccountDto platformAccountDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String imageId = platformAccountDto.getImageId();
        String imageCode = platformAccountDto.getCode();
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(imageId, imageCode)) {
            log.warn("请求参数不合法，请将参数填写完整");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "验证码填写错误,请输入正确的验证码！");
        }
        platformAccountService.verifyImagesCode(platformSystem.getCode(), imageId, imageCode);
        return Result.success();
    }

    @Override
    public Object verifyImageCodeLogin(PlatformAccountDto platformAccountDto,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        PlatformHttpHeadersDto platformHttpHeadersDto = platformHttpHeadersService.build(request);
        String ip = platformHttpHeadersDto.getClientIp();
        String code = platformHttpHeadersDto.getClientBusinessSource();
        PlatformSystem platformSystem = platformAccountManager.findBySystemCode(code);
        String imageId = platformAccountDto.getImageId();
        String account = platformAccountDto.getAccount();
        String pwd = platformAccountDto.getPwd();
        String imagesCode = platformAccountDto.getCode();
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(imageId, imagesCode, code, account, pwd)) {
            log.warn("请求参数不合法，请将参数填写完整");
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不完整，请将参数填写完整。");
        }
        verifyImageStatus(platformAccountDto, request, response);
        // TODO 这里要判断是手机号登录还是email登录
        PlatformAccount platformAccount = platformAccountDao.findByAccountAndSystemCode(account, platformSystem.getAccountLibraryCode());
        if (null == platformAccount) {
            log.error("账号不存在 account {} 未注册", account);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "账号或密码填写错误！");
        }
        if (platformAccount.getIsEffective() != 1) {
            log.error("账号不可用 account {} 账号被停用", account);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "账号被停用,请联系客户人员");
        }
        if (!platformAccount.getPassword().equals(EncryptPassword.encrypt(pwd))) {
            log.error("密码填写错误 phone {} ", account);
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "账号或密码填写错误！");
        }
        AuthUserInfoDto authUserInfoDto = AuthUserInfoDto.builder()
                .phone(account)
                .sourceType(1)
                .build();
        return loginSuccess(platformAccount.getId(), platformSystem.getAccountLibraryCode(), authUserInfoDto);
    }

    /**
     * 登录成功
     *
     * @param accountId
     * @param systemCode
     * @param authUserInfoDto
     * @return
     */
    private Object loginWapSuccess(Long accountId, String systemCode, AuthUserInfoDto authUserInfoDto) {
        PlatformAccountAuthResponse platformAccountAuthResponse = null;
        String uuid = UUID.randomUUID().toString();
        Long time = System.currentTimeMillis();
        String key = platformAccountCacheKeyTools.getAccountOauthUserInfoKey(uuid);
        if (redisCacheService.setStr(key, JsonTool.toJson(authUserInfoDto), PlatformAccountCacheKey.EXPIRATION)) {
            platformAccountAuthResponse = PlatformAccountAuthResponse.builder()
                    .accountId(accountId)
                    .uid(initUid(accountId))
                    .code(uuid)
                    .systemCode(systemCode)
                    .timestamp(time)
                    .build();
        }
        String code = Base64.getEncoder().encodeToString(JsonTool.toJson(platformAccountAuthResponse).getBytes());
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("openId", authUserInfoDto.getOpenIp());
        return Result.success(map);
    }

    /**
     * 登录成功
     *
     * @param accountId
     * @param systemCode
     * @param authUserInfoDto
     * @return
     */
    private Object loginSuccess(Long accountId, String systemCode, AuthUserInfoDto authUserInfoDto) {
        PlatformAccountAuthResponse platformAccountAuthResponse = null;
        String uuid = UUID.randomUUID().toString();
        Long time = System.currentTimeMillis();
        String key = platformAccountCacheKeyTools.getAccountOauthUserInfoKey(uuid);
        if (redisCacheService.setStr(key, JsonTool.toJson(authUserInfoDto), PlatformAccountCacheKey.EXPIRATION)) {
            platformAccountAuthResponse = PlatformAccountAuthResponse.builder()
                    .accountId(accountId)
                    .uid(initUid(accountId))
                    .code(uuid)
                    .systemCode(systemCode)
                    .timestamp(time)
                    .build();
        }
        String code = Base64.getEncoder().encodeToString(JsonTool.toJson(platformAccountAuthResponse).getBytes());
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        return Result.success(map);
    }

    /**
     * 登录成功
     *
     * @param accountId
     * @param systemCode
     * @param authUserInfoDto
     * @return
     */
    private Object loginSuccess(Long accountId, String systemCode, String openId, AuthUserInfoDto authUserInfoDto) {
        PlatformAccountAuthResponse platformAccountAuthResponse = null;
        String uuid = UUID.randomUUID().toString();
        Long time = System.currentTimeMillis();
        String key = platformAccountCacheKeyTools.getAccountOauthUserInfoKey(uuid);
        if (redisCacheService.setStr(key, JsonTool.toJson(authUserInfoDto), PlatformAccountCacheKey.EXPIRATION)) {
            platformAccountAuthResponse = PlatformAccountAuthResponse.builder()
                    .accountId(accountId)
                    .uid(initUid(accountId))
                    .code(uuid)
                    .systemCode(systemCode)
                    .timestamp(time)
                    .build();
        }
        String code = Base64.getEncoder().encodeToString(JsonTool.toJson(platformAccountAuthResponse).getBytes());
        Map<String, Object> map = new HashMap<>();
        map.put("openId", openId);
        map.put("code", code);
        map.put("userState", true);
        return Result.success(map);
    }

    /**
     * 登录成功
     *
     * @param accountId
     * @param systemCode
     * @param authUserInfoDto
     * @return
     */
    private String createCodeLoginSuccess(Long accountId, String systemCode, AuthUserInfoDto authUserInfoDto) {
        PlatformAccountAuthResponse platformAccountAuthResponse = null;
        String uuid = UUID.randomUUID().toString();
        Long time = System.currentTimeMillis();
        String key = platformAccountCacheKeyTools.getAccountOauthUserInfoKey(uuid);
        if (redisCacheService.setStr(key, JsonTool.toJson(authUserInfoDto), PlatformAccountCacheKey.EXPIRATION)) {
            platformAccountAuthResponse = PlatformAccountAuthResponse.builder()
                    .accountId(accountId)
                    .uid(initUid(accountId))
                    .code(uuid)
                    .systemCode(systemCode)
                    .timestamp(time)
                    .build();
        }
        return Base64.getEncoder().encodeToString(JsonTool.toJson(platformAccountAuthResponse).getBytes());
    }

    /**
     * 创建绑定信息
     *
     * @param platformAccountBindingInfoEnum 绑定类型
     * @param status                         是否开放绑定
     * @param bindingStatus                  绑定状态
     * @param bindingInfo                    绑定消息 （可以设置绑定的第三方昵称或者 绑定状态）
     * @return
     */
    private PlatformAccountBindingInfoDto createByPlatformAccountBindingInfoEnumAndStatusAndBindingStatus(
            PlatformAccountBindingInfoEnum platformAccountBindingInfoEnum,
            Boolean status,
            Boolean bindingStatus,
            String bindingInfo
    ) {
        return PlatformAccountBindingInfoDto.builder()
                .code(platformAccountBindingInfoEnum.getCode())
                .name(platformAccountBindingInfoEnum.getName())
                .status(status)
                .bindingStatus(bindingStatus)
                .bindingInfo(bindingInfo)
                .build();
    }

    private String initUid(Long accountId) {
        return String.valueOf((1000000 + accountId));
    }
}
