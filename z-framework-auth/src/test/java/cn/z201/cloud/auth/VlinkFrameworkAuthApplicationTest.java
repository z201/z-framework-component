package cn.z201.cloud.auth;

import cn.z201.cloud.alarm.core.AlarmNoticeManage;
import cn.z201.cloud.auth.entity.PlatformAccount;
import cn.z201.cloud.auth.manager.PlatformAccountCacheManagerI;
import cn.z201.cloud.auth.dao.PlatformAccountDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZFrameworkAuthApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dshop-test")
public class VlinkFrameworkAuthApplicationTest {

    @Autowired
    AlarmNoticeManage alarmNoticeManage;


    @Autowired
    PlatformAccountCacheManagerI platformAccountCacheManager;

    @Resource
    PlatformAccountDao platformAccountDao;


    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Test
    public void alarm() {
//        alarmNoticeManage.createNotice(new IllegalAccessException(),"测试默认预警","");
//
//        alarmNoticeManage.createNotice(new IllegalAccessException(), "测试单人预警", "lx");

//        alarmNoticeManage.createNoticeMany(new IllegalAccessException(),"测试多人预警","zqf","wzn","zss");

//        alarmNoticeManage.createNoticeAll(new IllegalAccessException(),"测试全体预警");

        List<PlatformAccount> platformAccountList = platformAccountDao.listAll();
        if (null != platformAccountList && !platformAccountList.isEmpty()) {
            for (PlatformAccount platformAccount : platformAccountList) {
                System.out.println(platformAccount);
                platformAccountCacheManager.refreshPlatformAccountCache(platformAccount);
            }
        }
//        ServiceInstance serviceInstance = loadBalancerClient.choose("dshop-user-rpc");
//        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort();
//        System.out.println("url" + url);
    }


}