package cn.z201.cloud.auth.controller;

import cn.z201.cloud.auth.dto.PlatformAccountDto;
import org.junit.Test;

/**
 * @author z201.coding@gmail.com
 **/
public class PlatformAccountWapControllerTest {


    //        public String server = "http://localhost:62430";
    public String server = "http://122.224.34.237:9000/api/v1/platform/";


    @Test
    public void accountWapOauth() {
        String url = server + "/authorization/account/wap/oauth";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .code("hqix")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
