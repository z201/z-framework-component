package cn.z201.cloud.auth.manager.impl;

import com.google.gson.Gson;
import cn.z201.cloud.auth.dao.PlatformAccountDao;
import cn.z201.cloud.auth.dto.AuthUserInfoDto;
import cn.z201.cloud.auth.entity.PlatformAccount;
import cn.z201.cloud.auth.manager.PlatformAccountCacheManagerI;
import cn.z201.cloud.auth.manager.RedisCacheServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Service
public class PlatformAccountCacheManagerImpl implements PlatformAccountCacheManagerI {

    @Autowired
    RedisCacheServiceI redisCacheService;

    @Autowired
    PlatformAccountCacheKeyTools platformAccountCacheKeyTools;

    @Resource
    PlatformAccountDao platformAccountDao;

    @Override
    public AuthUserInfoDto refreshPlatformAccountCache(PlatformAccount platformAccount) {
        String key = platformAccountCacheKeyTools.getAccountAuthInfoKey(platformAccount.getId());
        redisCacheService.del(key);
        AuthUserInfoDto authUserInfoDto = AuthUserInfoDto.builder()
                .accountId(platformAccount.getId())
                .phone(platformAccount.getPhoneNumber())
                .email(platformAccount.getEmail())
                .build();
        redisCacheService.setStr(key, new Gson().toJson(authUserInfoDto));
        return authUserInfoDto;
    }

    @Override
    public AuthUserInfoDto refreshPlatformAccountCache(Long accountId) {
        PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
        return refreshPlatformAccountCache(platformAccount);
    }

    @Override
    public AuthUserInfoDto getPlatformAccountCache(Long accountId) {
        String key = platformAccountCacheKeyTools.getAccountAuthInfoKey(accountId);
        AuthUserInfoDto authUserInfoDto = redisCacheService.getStr(key, AuthUserInfoDto.class);
        if (null == authUserInfoDto) {
            PlatformAccount platformAccount = platformAccountDao.selectByPrimaryKey(accountId);
            if (null == platformAccount) {
                return null;
            }
            return refreshPlatformAccountCache(platformAccount);
        }
        return authUserInfoDto;
    }

    @Override
    public List<AuthUserInfoDto> listLimitPlatformAccountCache(List<Long> accountIds) {
        if (null == accountIds || accountIds.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, AuthUserInfoDto> accountMaps = new LinkedHashMap<>();
        for (int i = 0; i < accountIds.size(); i++) {
            accountMaps.put(accountIds.get(i), AuthUserInfoDto.builder().accountId(accountIds.get(i)).build());
        }
        List<String> keys = accountIds.stream()
                .filter(Objects::nonNull)
                .map(platformAccountCacheKeyTools::getAccountAuthInfoKey)
                .collect(Collectors.toList());
        if (keys.isEmpty()) {
            return Collections.emptyList();
        }
        List<AuthUserInfoDto> list = redisCacheService.mGet(keys, AuthUserInfoDto.class);
        if (null == list || list.isEmpty()) {
            return Collections.emptyList();
        }
        AuthUserInfoDto authUserInfoDtoTemp = null;
        for (AuthUserInfoDto authUserInfoDto : list) {
            authUserInfoDtoTemp = accountMaps.get(authUserInfoDto.getAccountId());
            if (null == authUserInfoDtoTemp) {
                authUserInfoDtoTemp = refreshPlatformAccountCache(authUserInfoDto.getAccountId());
                if (null == authUserInfoDtoTemp) {

                }
            }else{

            }
        }
        return list;
    }
}
