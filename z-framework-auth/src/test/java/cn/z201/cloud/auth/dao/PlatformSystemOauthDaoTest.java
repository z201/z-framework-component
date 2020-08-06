package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformSystemOauth;
import cn.z201.cloud.auth.ZFrameworkAuthApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZFrameworkAuthApplication.class)
@ActiveProfiles("dev")
public class PlatformSystemOauthDaoTest {

    @Resource
    PlatformSystemOauthDao platformSystemOauthDao;

    @Test
    public void insertSelective() {
        Long time = System.currentTimeMillis();
        PlatformSystemOauth platformSystemOauth = PlatformSystemOauth.builder()
                .key("wx477484135c0f41fd")
                .secretId("d22513810b8a081e4d52a539cc1bf2ef")
                .enable(1)
                .oauthType(1)
                .updateTime(time)
                .createTime(time)
                .build();
        platformSystemOauthDao.insertSelective(platformSystemOauth);
    }

    @Test
    public void selectByPrimaryKey() {


    }

    @Test
    public void updateByPrimaryKeySelective() {


    }

    @Test
    public void findBySystemIdAndOauthType() {
        PlatformSystemOauth platformSystemOauth = platformSystemOauthDao.findBySystemCodeAndOauthType("", 1);


    }
}