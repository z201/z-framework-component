package cn.z201.cloud.auth.controller;

import cn.z201.cloud.auth.dto.PlatformAccountDto;
import org.junit.Test;

public class PlatformAccountWebControllerTest {

//        public String server = "http://localhost:62430";
    public String server = "http://122.224.34.237:9000/api/v1/platform/";


    @Test
    public void verifyImageStatus() {
        String url = server + "/authorization/verify/image/check";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .imageId("a792bc51-4d96-4326-9cca-b0e46e96c3e9")
                .code("-1")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verifyImageCodeLogin() {
        String url = server + "/authorization/verify/image/login";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .imageId("a792bc51-4d96-4326-9cca-b0e46e96c3e9")
                .code("-1")
                .account("13677173510")
                .pwd("123456")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void login(){
        String url = "http://122.224.34.237:9000/api/v1/web/space/info/login";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .code("eyJhY2NvdW50SWQiOjU2LCJjb2RlIjoiN2FmMjg1ZTktODBjOC00MWUxLWFjOTktNDdlNTEwOTUyMzMxIiwic3lzdGVtQ29kZSI6IkZTMDAwMyIsInRpbWVzdGFtcCI6MTU3NTQyODgwOTM3OH0=")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
