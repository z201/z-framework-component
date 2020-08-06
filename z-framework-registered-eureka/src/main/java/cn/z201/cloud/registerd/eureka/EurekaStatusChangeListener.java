package cn.z201.cloud.registerd.eureka;

import cn.z201.cloud.alarm.core.AlarmNoticeManage;
import cn.z201.cloud.alarm.core.dto.ServiceNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author z201.coding@gmail.com
 **/
@Component
public class EurekaStatusChangeListener {

    @Autowired
    AlarmNoticeManage alarmNoticeManage;

    @Value("${spring.profiles.active}")
    private String active;

    private static final Logger log = LoggerFactory.getLogger(EurekaStatusChangeListener.class);

    @EventListener
    public void listen(EurekaInstanceCanceledEvent event) {
        log.error("告警通知 [{}] 服务注销 timestamp [{}] serverId [{}]",
                event.getAppName(),
                event.getTimestamp(),
                event.getServerId());
        if (!event.isReplication()) {
            ServiceNotice serviceNotice = new ServiceNotice();
            serviceNotice.setTitle("告警通知 服务下线");
            serviceNotice.setAppName(event.getAppName());
            serviceNotice.setTimestamp(event.getTimestamp());
            serviceNotice.setServerId(event.getServerId());
            serviceNotice.setProfiles(active);
            alarmNoticeManage.createNotice(serviceNotice, "zqf");
        }
    }

    @EventListener
    public void listen(EurekaInstanceRegisteredEvent event) {
        log.info("Eureka服务端可用事件");
        log.info("告警通知 [{}] 服务注册 timestamp [{}] id [{}] ipAddr [{}] ",
                event.getInstanceInfo().getAppName(),
                event.getTimestamp(),
                event.getInstanceInfo().getId(),
                event.getInstanceInfo().getIPAddr()
        );
        if (!event.isReplication()) {
            ServiceNotice serviceNotice = new ServiceNotice();
            serviceNotice.setTitle("告警通知 服务上线");
            serviceNotice.setAppName(event.getInstanceInfo().getAppName());
            serviceNotice.setTimestamp(event.getTimestamp());
            serviceNotice.setServerId(event.getInstanceInfo().getId());
            serviceNotice.setProfiles(active);
            alarmNoticeManage.createNotice(serviceNotice, "zqf");
        }
    }

    @EventListener
    public void listen(EurekaInstanceRenewedEvent event) {
        log.info("服务进行续约 {} {} ", event.getServerId(), event.getAppName());
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        log.info("Eureka服务端可用事件");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        log.info("Eureka Server 启动");
    }
}
