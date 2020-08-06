package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.ZFrameworkAuthApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZFrameworkAuthApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class PlatformAccountDaoTest {

    @Resource
    PlatformAccountDao platformAccountDao;

    @Test
    public void insertSelective() {

    }

    @Test
    public void selectByPrimaryKey() {
    }

    @Test
    public void updateByPrimaryKeySelective() {

    }

    @Test
    public void findByAccountAndSystemCode(){
        platformAccountDao.findByAccountAndSystemCode("admin@hongdoujiao.com","FS0002");
    }
}

