package cn.z201.cloud.auth;

import cn.z201.cloud.auth.dto.TokenInfo;
import com.google.gson.Gson;
import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
public class JwtTokenUtils {

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final Long DEF_RENEWAL_TIMESTAMP = 1800L;

    private static final Long DEF_CHECK_INTERVAL = 1800L;

    private static final Long DEF_EXPIRATION = 1800L;

    private static final String SECRET = "vlink.token";

    private static final String DEF_ISS = "http://www.hongdoujiao.com/";

    /**
     * 令牌用户信息
     */
    private static final String U_DATA = "U";

    /**
     * 签发token
     *
     * @param accountId        平台账号id
     * @param userId           系统用户id
     * @param systemCodeArr    访问系统权限code数组
     * @param checkInterval    jwt检查刷新间隔
     * @param renewalTimestamp jwt续期时间
     * @param expiration       jwt过期时间
     * @return
     */
    public static String issueJwt(Long accountId,
                                  Long userId,
                                  String[] systemCodeArr,
                                  Long checkInterval,
                                  Long renewalTimestamp,
                                  Long expiration) {
        Assert.notNull(systemCodeArr, "systemCodeArr is null");
        return issueJwt(accountId, userId, systemCodeArr[0], systemCodeArr, checkInterval, renewalTimestamp, expiration);
    }

    /**
     * 签发token
     *
     * @param accountId        平台账号id
     * @param userId           系统用户id
     * @param systemCodeArr    访问系统权限code数组
     * @param checkInterval    jwt检查刷新间隔
     * @param renewalTimestamp jwt续期时间
     * @param expiration       jwt过期时间
     * @return
     */
    public static String issueJwt(Long accountId,
                                  Long userId,
                                  String loginSystemCode,
                                  String[] systemCodeArr,
                                  Long checkInterval,
                                  Long renewalTimestamp,
                                  Long expiration) {
        return issueJwt(accountId, userId, loginSystemCode, systemCodeArr, true, checkInterval, renewalTimestamp, expiration);
    }

    /**
     * 签发token
     *
     * @param accountId        平台账号id
     * @param userId           系统用户id
     * @param systemCodeArr    访问系统权限code数组
     * @param enableOnlyOnline 启用唯一在线
     * @param checkInterval    jwt检查刷新间隔
     * @param renewalTimestamp jwt续期时间
     * @param expiration       jwt过期时间
     * @return
     */
    public static String issueJwt(Long accountId,
                                  Long userId,
                                  String loginSystemCode,
                                  String[] systemCodeArr,
                                  Boolean enableOnlyOnline,
                                  Long checkInterval,
                                  Long renewalTimestamp,
                                  Long expiration) {
        Assert.notNull(accountId, "accountId is null");
        Assert.notNull(userId, "userId is null");
        Assert.notNull(loginSystemCode, "loginSystemCode is null");
        Assert.notNull(systemCodeArr, "systemCodeArr is null");
        Assert.notNull(enableOnlyOnline, "enableOnlyOnline is null");
        Assert.notNull(checkInterval, "checkInterval is null");
        Assert.notNull(renewalTimestamp, "renewalTimestamp is null");
        Assert.notNull(expiration, "expiration is null");
        TokenInfo tokenInfo = TokenInfo.builder()
                .accountId(accountId)
                .userId(userId)
                .loginSystemCode(loginSystemCode)
                .systemCodeSet(new HashSet<>(Arrays.asList(systemCodeArr)))
                .enableOnlyOnline(enableOnlyOnline)
                .renewalTimestamp(renewalTimestamp)
                .checkInterval(checkInterval)
                .expiration(expiration)
                .build();
        return issueJwt(tokenInfo);
    }

    public static String issueJwt(TokenInfo tokenInfo) {
        Gson gson = new Gson();
        String u = gson.toJson(tokenInfo);
        HashMap<String, Object> map = new HashMap<>();
        map.put(U_DATA, u);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(DEF_ISS)
                .setSubject(String.valueOf(tokenInfo.getAccountId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenInfo.getExpiration() * 1000))
                .compact();
    }


    public static TokenInfo getTokenInfo(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.trim())
                    .getBody();
        } catch (Exception e) {
            log.warn("token 检查失败,无法刷新token。");
            return null;
        }
        Gson gson = new Gson();
        Object obj = claims.get(U_DATA);
        if (null == obj) {
            return null;
        }
        return gson.fromJson(obj.toString(), TokenInfo.class);
    }

    /**
     * 检查是否过期
     *
     * @param jwt true 过期 false未过期
     * @return
     */
    public static boolean checkJwtExpiration(String jwt) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(jwt.trim())
                    .getBody();
        } catch (RuntimeException e) {
            return true;
        }
        if (null == claims) {
            return true;
        }
        try {
            return claims.getExpiration().before(new Date());
        } catch (RuntimeException ex) {
            log.warn("jwt 过期啦～～～～");
        }
        return true;
    }


    public static String refresh(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.trim())
                    .getBody();
        } catch (Exception e) {
            log.warn("token 检查失败,无法刷新token。");
            return null;
        }
        try {
            claims.getExpiration().before(new Date());
        } catch (RuntimeException ex) {
            log.warn("jwt 过期啦～～～～");
            return null;
        }
        Object u = claims.get(U_DATA);
        if (null == u) {
            log.warn("jwt 无效 ～～～～");
            return null;
        }
        Gson gson = new Gson();
        TokenInfo tokenInfo = gson.fromJson(u.toString(), TokenInfo.class);
        Long expiration = tokenInfo.getExpiration();
        HashMap<String, Object> map = new HashMap<>();
        map.put(U_DATA, u);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(map)
                .setIssuer(DEF_ISS)
                .setSubject(String.valueOf(tokenInfo.getAccountId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     * 创建默认条件到U_DATA
     *
     * @param accountId  平台账号id
     * @param userId     系统用户id
     * @param systemCode 访问系统权限code
     * @return
     */
    public static TokenInfo createTokenInfo(Long accountId, Long userId, String systemCode) {
        return TokenInfo.builder()
                .accountId(accountId)
                .userId(userId)
                .systemCodeSet(new HashSet<>(Arrays.asList(systemCode)))
                .renewalTimestamp(DEF_RENEWAL_TIMESTAMP)
                .checkInterval(DEF_CHECK_INTERVAL)
                .expiration(DEF_EXPIRATION)
                .build();
    }

    /**
     * 创建默认条件到U_DATA
     *
     * @param accountId     平台账号id
     * @param userId        系统用户id
     * @param systemCodeArr 访问系统权限code数组
     * @return
     */
    public static TokenInfo createTokenInfo(Long accountId, Long userId, String[] systemCodeArr) {
        return TokenInfo.builder()
                .accountId(accountId)
                .userId(userId)
                .systemCodeSet(new HashSet<>(Arrays.asList(systemCodeArr)))
                .renewalTimestamp(DEF_RENEWAL_TIMESTAMP)
                .checkInterval(DEF_CHECK_INTERVAL)
                .expiration(DEF_EXPIRATION)
                .build();
    }

}
