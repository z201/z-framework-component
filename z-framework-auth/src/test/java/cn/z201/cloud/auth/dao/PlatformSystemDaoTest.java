package cn.z201.cloud.auth.dao;

import cn.z201.cloud.auth.entity.PlatformSystem;
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
public class PlatformSystemDaoTest {

    @Resource
    PlatformSystemDao platformSystemDao;

    @Test
    public void insertSelective() {
        Long time = System.currentTimeMillis();
        PlatformSystem platformSystem = PlatformSystem.builder()
                .title("分闪app")
                .businessGroup("FS")
                .code("FS0001")
                .type(100)
                .desc("分闪app")
                .isEffective(1)
                .updateTime(time)
                .createTime(time)
                .build();
        platformSystemDao.insertSelective(platformSystem);
    }

    @Test
    public void selectByPrimaryKey() {

    }

    @Test
    public void updateByPrimaryKeySelective() {

    }

    @Test
    public void findBySystemCode(){
        PlatformSystem platformSystem = platformSystemDao.findBySystemCode("FS0001");
    }
}