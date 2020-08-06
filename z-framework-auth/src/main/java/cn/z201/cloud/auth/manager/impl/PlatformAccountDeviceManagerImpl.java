package cn.z201.cloud.auth.manager.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cn.z201.cloud.auth.dao.PlatformAccountDao;
import cn.z201.cloud.auth.dao.PlatformAccountDeviceUmengDao;
import cn.z201.cloud.auth.dao.PlatformAccountOauthDao;
import cn.z201.cloud.auth.dao.PlatformSystemOauthDao;
import cn.z201.cloud.auth.dto.PlatformUserMessageDataSourceDto;
import cn.z201.cloud.auth.dto.PlatformUserMessageDetailsDataSourceDto;
import cn.z201.cloud.auth.entity.PlatformAccount;
import cn.z201.cloud.auth.entity.PlatformAccountDeviceUmeng;
import cn.z201.cloud.auth.entity.PlatformAccountOauth;
import cn.z201.cloud.auth.enums.PlatformAccountOauthEnum;
import cn.z201.cloud.auth.enums.PlatformUserMessageDetailsEnum;
import cn.z201.cloud.auth.manager.PlatformAccountCacheKey;
import cn.z201.cloud.auth.manager.PlatformAccountDeviceManagerI;
import cn.z201.cloud.auth.manager.RedisCacheServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Service
public class PlatformAccountDeviceManagerImpl implements PlatformAccountDeviceManagerI {

    @Autowired
    RedisCacheServiceI redisCacheService;

    @Autowired
    PlatformAccountCacheKey platformAccountCacheKey;

    @Resource
    PlatformAccountDeviceUmengDao platformAccountDeviceUmengDao;

    @Resource
    PlatformAccountOauthDao platformAccountOauthDao;

    @Resource
    PlatformAccountDao platformAccountDao;

    @Resource
    PlatformSystemOauthDao platformSystemOauthDao;

    @Override
    public PlatformUserMessageDataSourceDto getAccountDevice(String systemCode, Long accountId) {
        String key = platformAccountCacheKey.getAccountClientInfoKey(accountId);
        PlatformUserMessageDataSourceDto platformUserMessageDataSourceDto = redisCacheService.getStr(key, PlatformUserMessageDataSourceDto.class);
        if (null == platformUserMessageDataSourceDto) {
            PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
            if (null == platformAccount) {
                return null;
            }
            platformUserMessageDataSourceDto = refreshAccountDevice(platformAccount);
        }
        return platformUserMessageDataSourceDto;
    }

    @Override
    @Async
    public void refreshAccountDevice(Long accountId) {
        PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
        if (null == platformAccount) {
            return;
        }
        refreshAccountDevice(platformAccount);
    }

    private PlatformUserMessageDataSourceDto refreshAccountDevice(PlatformAccount platformAccount) {
        PlatformUserMessageDetailsDataSourceDto platformUserMessageDetailsDataSourceDto = null;
        PlatformUserMessageDataSourceDto platformUserMessageDataSourceDto = PlatformUserMessageDataSourceDto.builder().build();
        platformUserMessageDataSourceDto.setAccountId(platformAccount.getId());
        platformUserMessageDataSourceDto.setClientBusinessActivitySource(platformAccount.getSystemCode());
        PlatformAccountDeviceUmeng platformAccountDeviceUmeng = platformAccountDeviceUmengDao.findByAccountId(platformAccount.getId());
        if (null != platformAccountDeviceUmeng) {
            platformUserMessageDetailsDataSourceDto =
                    PlatformUserMessageDetailsDataSourceDto.builder().build();
            platformUserMessageDetailsDataSourceDto.setClientBusinessActivitySource(platformAccountDeviceUmeng.getSystemCode());
            platformUserMessageDetailsDataSourceDto.setUmengId(platformAccountDeviceUmeng.getDeviceId());
            platformUserMessageDetailsDataSourceDto.setPlatformUserMessageDetailsEnum(PlatformUserMessageDetailsEnum.U_MENG);
            platformUserMessageDetailsDataSourceDto.setUmengClientEnvSource(platformAccountDeviceUmeng.getClientEnvSource());
            platformUserMessageDataSourceDto.addPlatformUserMessageDetailsDataSourceDto(platformUserMessageDetailsDataSourceDto);
        }
        List<PlatformAccountOauth> platformAccountOauthList = platformAccountOauthDao.listByAccountId(platformAccount.getId());
        if (null != platformAccountOauthList && !platformAccountOauthList.isEmpty()) {
            for (PlatformAccountOauth platformAccountOauth : platformAccountOauthList) {
                if (null != platformAccountOauth
                        && null != platformAccountOauth.getOauthCode()
                        && null != platformAccountOauth.getOauthOpenId()) {
                    platformUserMessageDetailsDataSourceDto = PlatformUserMessageDetailsDataSourceDto.builder().build();
                    if (platformAccountOauth.getOauthCode().equals(PlatformAccountOauthEnum.MINI_PROGRAM_WE_CHAT.getCode())) {
                        platformUserMessageDetailsDataSourceDto.setPlatformUserMessageDetailsEnum(PlatformUserMessageDetailsEnum.WE_CHAT_MINI_PROGRAM);
                        platformUserMessageDetailsDataSourceDto.setOpenId(platformAccountOauth.getOauthOpenId());
                        platformUserMessageDetailsDataSourceDto.setUmengId(platformAccountOauth.getOauthOpenId());
                        platformUserMessageDataSourceDto.addPlatformUserMessageDetailsDataSourceDto(platformUserMessageDetailsDataSourceDto);
                    } else if (platformAccountOauth.getOauthCode().equals(PlatformAccountOauthEnum.WAP_WE_CHAT.getCode())) {
                        platformUserMessageDetailsDataSourceDto.setPlatformUserMessageDetailsEnum(PlatformUserMessageDetailsEnum.WE_CHAT_PUBLIC_ACCOUNT);
                        platformUserMessageDetailsDataSourceDto.setUmengId(platformAccountOauth.getOauthOpenId());
                        platformUserMessageDetailsDataSourceDto.setOpenId(platformAccountOauth.getOauthOpenId());
                        platformUserMessageDataSourceDto.addPlatformUserMessageDetailsDataSourceDto(platformUserMessageDetailsDataSourceDto);
                    }
                }
            }
        }
        String key = platformAccountCacheKey.getAccountClientInfoKey(platformAccount.getId());
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        redisCacheService.setStr(key, gson.toJson(platformUserMessageDataSourceDto));
        return platformUserMessageDataSourceDto;
    }
}
