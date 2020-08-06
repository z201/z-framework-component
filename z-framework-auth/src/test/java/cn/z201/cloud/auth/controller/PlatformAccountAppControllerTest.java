package cn.z201.cloud.auth.controller;

import cn.z201.cloud.auth.dto.PlatformAccountDto;
import cn.z201.cloud.encrypt.core.utils.AESEncryptOperator;
import org.junit.Test;

import java.util.Base64;

public class PlatformAccountAppControllerTest {

//            public String server = "http://localhost:64219";
    public String server = "http://api.test.fenshan.com.cn:90/api/v1/platform";

    @Test
    public void base64() {
//        String uuid = UUID.randomUUID().toString();
//        System.out.println("uuid "+new String(uuid));
//        String eStr = Base64.getEncoder().encodeToString(uuid.getBytes());
//        System.out.println("eStr "+new String(eStr));
//        byte[] oBytes = Base64.getDecoder().decode(eStr);
//        System.out.println("uuid "+new String(oBytes));
//        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
//                .phone("15715815072")
//                .pwd("11")
//                .build();
//        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//        String body = gson.toJson(platformAccountDto);
//        System.out.println("json "+ body);
//        Long timestamp = 1571751962711L;
//        System.out.println("timestamp  " + timestamp);
//        String sign = DigestUtils.md5Hex((timestamp + body));
//        System.out.println("sign " + sign);
//        body = AESEncryptOperator.encrypt(body);
//        System.out.println("aes json  "+  body);
//        body = Base64.getEncoder().encodeToString(body.getBytes());
//        System.out.println("base64 aes json  "+ body);
//        // 那你试试 加一个+号
//        platformAccountDto = PlatformAccountDto.builder()
//                .phone("13677173510")
//                .pwd("1111")
//                .build();
//        body = gson.toJson(platformAccountDto);
//        String json = 1571883063695L+body;
//        System.out.println("json  "+ json );
//        String a = DigestUtils.md5Hex(json);
//        System.out.println("md5 json  "+ a);


//       String data =  "{\"productStatus\":1,\"productImage\":[{\"picId\":1,\"sortNum\":1}],\"productName\":\"测试无规格\",\"productCategoryId\":3,\"productSkuInventory\":\"[{\\\"inventory\\\":\\\"10000\\\",\\\"optionalIds\\\":0,\\\"marketPrice\\\":\\\"200\\\",\\\"cast\\\":\\\"100\\\",\\\"picId\\\":1}]\",\"srotageAlarm\":\"101\",\"productNumber\":\"11115454\",\"productDesc\":\"<!DOCTYPE HTML PUBLIC \\\"-//W3C//DTD HTML 4.01 Transitional//EN\\\" \\\"http://www.w3.org/TR/html4/loose.dtd\\\"><html><head><meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html;charset=UTF-8\\\"><style>.my-ql{padding:0;}img{max-width:100%!important;}</style></head><body><div class=\\\"ql-editor my-ql\\\"><p class=\\\"ql-align-center\\\"><strong class=\\\"ql-size-large\\\" style=\\\"color: rgb(240, 102, 102);\\\"><em>撒地方撒地方撒地方是的水电费水电费sdf</em></strong></p><p class=\\\"ql-align-center\\\"><strong class=\\\"ql-size-large\\\" style=\\\"color: rgb(240, 102, 102);\\\"><em>撒的发生sdf 说的</em></strong></p><p class=\\\"ql-align-center\\\"><strong class=\\\"ql-size-large\\\" style=\\\"color: rgb(240, 102, 102);\\\"><em>sd</em></strong></p><p class=\\\"ql-align-center\\\"><strong class=\\\"ql-size-large\\\" style=\\\"color: rgb(240, 102, 102);\\\"><em> dzxvxzcvz</em></strong></p><p class=\\\"ql-align-center\\\"><strong class=\\\"ql-size-large\\\" style=\\\"color: rgb(240, 102, 102);\\\"><em>在</em></strong></p></div></body></html>\",\"productCommitment\":[{\"commitmentId\":1,\"name\":\"七天无理由退货\"}],\"productTemplateId\":1}";
//       Long time =  1573009530964L;
//        System.out.println(DigestUtils.md5Hex((time + data)));
        String data = new String(Base64.getDecoder().decode("MEU3bjdNRjdIUmVTcm9zN0wrQVlPQT09"));
        System.out.println(data);
        data = AESEncryptOperator.decrypt(data);
        System.out.println(data);
    }


    @Test
    public void accountRpcInfo() {
        String url = server + "/authorization/rpc/account/info";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .code("eyJhY2NvdW50SWQiOjIxMCwidWlkIjoiMTAwMDIxMCIsImNvZGUiOiJlZDI2ZGM5MS1jZmM0LTQ2NzgtYTE2Yi1hZDBhNzQzOWUxNjkiLCJzeXN0ZW1Db2RlIjoiMDAzMTAwMCIsInRpbWVzdGFtcCI6MTU3NjQ2MzEwNTMyM30=")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void accountOauth() {
        String url = server + "/platform/authorization/account/oauth";
//        String url = server + "/authorization/account/oauth";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .code("081CRVh22jfTSV0tvSi22d5Uh22CRVhe")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void accountMiniProgramOauth() {
        String url = server + "/platform/authorization/account/mini/program/oauth";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .code("081aiSue2BL4aF0qWHte2fV2ve2aiSuo")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void accountMiniProgramRegistered() {
        String url = server + "/platform/authorization/account/mini/program/registered";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .openId("081aiSue2BL4aF0qWHte2fV2ve2aiSuo")
                .iv("")
                .encryptedData("")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void accountBindingInfo() {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2OCIsIlUiOiJ7XCJhY2NvdW50SWRcIjo2OCxcInVzZXJJZFwiOjE2LFwic3lzdGVtQ29kZVNldFwiOltcIkZTMDAwMVwiXSxcInJlbmV3YWxUaW1lc3RhbXBcIjozNjAwLFwiY2hlY2tJbnRlcnZhbFwiOjM2MDAsXCJleHBpcmF0aW9uXCI6NzIwMDAwMDB9IiwiaXNzIjoiaHR0cDovL3d3dy5ob25nZG91amlhby5jb20vIiwiZXhwIjoxNjQ2NzU2NTk1LCJpYXQiOjE1NzQ3NTY1OTV9.sspIaGnTPpi81PlNrII51g6yTWHHtw1RigydXsmaHcdXtbFkovdYdyLl-_txwiGBiGBtwbfag7YvGm-mJurR_Q";
        String url = server + "/platform/authorization/account/binding/info";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .build();
        try {
            TestUtils.postEncrypt(url, jwt, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bindingOauthSignIn() {

    }

    @Test
    public void untiedOauthSignIn() {

    }

    @Test
    public void phoneSendSms() {
        String url = server + "/authorization/phone/sms/send";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .phone("18973101800")
                .type(1)
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void phoneCheckSms() {
        String url = server + "/authorization/phone/sms/check";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .phone("18973101800")
                .type(1)
                .smsCode("8888")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void phoneSmsLogin() {
        String url = server + "/authorization/phone/sms/login";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .phone("18973101800")
                .smsCode("8888")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void login() {
        String url =  "http://api.test.fenshan.com.cn:90/api/v1/mobile/user/info/login";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .code("eyJhY2NvdW50SWQiOjE1OSwidWlkIjoiMTAwMDE1OSIsImNvZGUiOiJlZTg2YmU5OS0xYzE3LTRhYzEtOTVkNS1hYTI5NTU2MThhMjMiLCJzeXN0ZW1Db2RlIjoiMDAzMTAwMCIsInRpbWVzdGFtcCI6MTU3NTcyNzQyMjQxM30=")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void phonePasswordLogin() {
        String url = server + "/authorization/phone/password/login";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .phone("13677173510")
                .pwd("123456789")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void smsCodePasswordForget() {
        String url = server + "/authorization/phone/sms/password/forget";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .phone("13677173510")
                .pwd("1234567")
                .pwd2("1234567")
                .smsCode("8888")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void changePassword() {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0OCIsIlUiOiJ7XCJhY2NvdW50SWRcIjo0OCxcInVzZXJJZFwiOjEsXCJzeXN0ZW1Db2RlU2V0XCI6W1wiRlMwMDAxXCJdLFwicmVuZXdhbFRpbWVzdGFtcFwiOjM2MDAsXCJjaGVja0ludGVydmFsXCI6MzYwMCxcImV4cGlyYXRpb25cIjo3MjAwMH0iLCJpc3MiOiJodHRwOi8vd3d3Lmhvbmdkb3VqaWFvLmNvbS8iLCJleHAiOjE1NzI0MDA3NjMsImlhdCI6MTU3MjMyODc2M30.D-OnbkdU8BIa5I8yGPL_dgEgowYOnUQ22Q0ZxJPcWdFgg4JSSyuGEjlmzODe17DdCpnYmTlid6v6mAkj1Buq4w";
        String url = server + "/platform/authorization/phone/password/change";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .pwd("123456")
                .pwd2("123456")
                .smsCode("8888")
                .build();
        try {
            TestUtils.postEncrypt(url, jwt, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void changeBindingPhone() {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0OCIsIlUiOiJ7XCJhY2NvdW50SWRcIjo0OCxcInVzZXJJZFwiOjEsXCJzeXN0ZW1Db2RlU2V0XCI6W1wiRlMwMDAxXCJdLFwicmVuZXdhbFRpbWVzdGFtcFwiOjM2MDAsXCJjaGVja0ludGVydmFsXCI6MzYwMCxcImV4cGlyYXRpb25cIjo3MjAwMH0iLCJpc3MiOiJodHRwOi8vd3d3Lmhvbmdkb3VqaWFvLmNvbS8iLCJleHAiOjE1NzI0MDA3NjMsImlhdCI6MTU3MjMyODc2M30.D-OnbkdU8BIa5I8yGPL_dgEgowYOnUQ22Q0ZxJPcWdFgg4JSSyuGEjlmzODe17DdCpnYmTlid6v6mAkj1Buq4w";
        String url = server + "/platform/authorization/phone/binding/change";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .smsCode("8888")
                .newPhone("13677173512")
                .newPhoneSmsCode("8888")
                .build();
        try {
            TestUtils.postEncrypt(url, jwt, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bindingPhone() {
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2OCIsIlUiOiJ7XCJhY2NvdW50SWRcIjo2OCxcInVzZXJJZFwiOjE2LFwic3lzdGVtQ29kZVNldFwiOltcIkZTMDAwMVwiXSxcInJlbmV3YWxUaW1lc3RhbXBcIjozNjAwLFwiY2hlY2tJbnRlcnZhbFwiOjM2MDAsXCJleHBpcmF0aW9uXCI6NzIwMDAwMDB9IiwiaXNzIjoiaHR0cDovL3d3dy5ob25nZG91amlhby5jb20vIiwiZXhwIjoxNjQ2NzU2NTk1LCJpYXQiOjE1NzQ3NTY1OTV9.sspIaGnTPpi81PlNrII51g6yTWHHtw1RigydXsmaHcdXtbFkovdYdyLl-_txwiGBiGBtwbfag7YvGm-mJurR_Q";
        String url = server + "/platform/authorization/phone/binding";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .smsCode("8888")
                .phone("15858100200")
                .build();
        try {
            TestUtils.postEncrypt(url, jwt, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void checkPhone() {
        String url = server + "/authorization/phone/check";
        PlatformAccountDto platformAccountDto = PlatformAccountDto.builder()
                .phone("13677173511")
                .build();
        try {
            TestUtils.postEncrypt(url, platformAccountDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}