package cn.z201.cloud.registerd.eureka;

import cn.z201.cloud.alarm.core.AlarmNoticeManage;
import cn.z201.cloud.alarm.core.dto.ServiceNotice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZFrameworkEurekaApplication.class)
@ActiveProfiles("prod1")
public class VlinkFrameworkEurekaApplicationTest {

    @Autowired
    AlarmNoticeManage alarmNoticeManage;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    public void alarm() {
        for (int i = 1; i < 2 ; i++) {
            String msg = "第" + i + "次";
            ServiceNotice serviceNotice = new ServiceNotice();
            serviceNotice.setTitle("正式获取可用机器人token发送预警");
            serviceNotice.setAppName(msg);
            serviceNotice.setTimestamp(System.currentTimeMillis());
            serviceNotice.setServerId(msg);
            serviceNotice.setProfiles(msg);
            alarmNoticeManage.createNotice(serviceNotice,"zqf");
        }

//        serviceNotice = new ServiceNotice();
//        serviceNotice.setTitle("多个人预警");
//        serviceNotice.setAppName("test");
//        serviceNotice.setTimestamp(System.currentTimeMillis());
//        serviceNotice.setServerId("test");
//        serviceNotice.setProfiles("test");
//        alarmNoticeManage.createNotice(serviceNotice,"zqf","wzn");
//        serviceNotice = new ServiceNotice();
//        serviceNotice.setTitle("全体预警");
//        serviceNotice.setAppName("test");
//        serviceNotice.setTimestamp(System.currentTimeMillis());
//        serviceNotice.setServerId("test");
//        serviceNotice.setProfiles("test");
//        alarmNoticeManage.createNoticeAll(serviceNotice);
    }

}