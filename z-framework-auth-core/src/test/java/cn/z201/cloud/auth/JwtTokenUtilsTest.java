package cn.z201.cloud.auth;

import cn.z201.cloud.auth.dto.TokenInfo;
import org.junit.Test;

public class JwtTokenUtilsTest {

    @Test
    public void createTokenInfo() {
        TokenInfo tokenInfo = JwtTokenUtils.createTokenInfo(1L,2L,"FS0001");
        String token = JwtTokenUtils.issueJwt(tokenInfo);
        System.out.println(token);
        System.out.println(JwtTokenUtils.checkJwtExpiration(token));
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(JwtTokenUtils.checkJwtExpiration(token));
        token = JwtTokenUtils.refresh(token);
        System.out.println(token);
        tokenInfo = JwtTokenUtils.getTokenInfo(token);
        System.out.println(tokenInfo);
    }

    @Test
    public void issueJwt() {

    }

    @Test
    public void issueJwt1() {
    }

    @Test
    public void getJwt() {
    }

    @Test
    public void checkJwtExpiration() {
    }

    @Test
    public void refresh() {
        TokenInfo tokenInfo = JwtTokenUtils.getTokenInfo("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1MCIsIlUiOiJ7XCJhY2NvdW50SWRcIjo1MCxcInVzZXJJZFwiOjIsXCJzeXN0ZW1Db2RlU2V0XCI6W1wiRlMwMDAxXCJdLFwicmVuZXdhbFRpbWVzdGFtcFwiOjM2MDAsXCJjaGVja0ludGVydmFsXCI6MzYwMCxcImV4cGlyYXRpb25cIjo3MjAwMH0iLCJpc3MiOiJodHRwOi8vd3d3Lmhvbmdkb3VqaWFvLmNvbS8iLCJleHAiOjE1NzIzMDAyMDQsImlhdCI6MTU3MjIyODIwNH0.QA0OCcXeh1q57FxOrnq0hoBpmFrH3Uty4dgZbuJLFifqGFOmRNVODndD_vpS9j3JZyeR0e1idk96juQ7Jrto1g");
        System.out.println(tokenInfo);

    }
}