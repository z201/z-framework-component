package cn.z201.cloud.alarm.core;

import cn.z201.cloud.alarm.core.manager.AlarmDingTalkWebHookTokenManager;
import cn.z201.cloud.alarm.core.property.AlarmNoticeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 * 预警通知配置
 **/
@Configuration
@EnableConfigurationProperties({AlarmNoticeProperty.class})
@ConditionalOnProperty(name = "alarm.open-notice", havingValue = "true", matchIfMissing = true)
public class AlarmNoticeConfig {

    private static final Logger log = LoggerFactory.getLogger(AlarmNoticeConfig.class);

    public AlarmNoticeConfig() {
        log.info("Loaded Z-Alarm [V1.0.0]");
    }

    @Autowired
    AlarmNoticeProperty alarmNoticeProperty;

    @Autowired
    AlarmDingTalkWebHookTokenManager alarmDingTalkWebHookTokenManager;

    @Autowired
    ContextRefresher contextRefresher;

    @Value("${spring.profiles.active}")
    private String active;

    @Bean
    @ConditionalOnMissingBean({AlarmNoticeManage.class})
    public AlarmNoticeManage alarmNoticeManage() throws IllegalAccessException {
        if (null != active) {
            List<String> dingTalkWebHook = null;
            if (active.contains("test") || active.contains("dev")) {
                if (null == alarmNoticeProperty.getDingTalkWebHookDevToken() ||
                        alarmNoticeProperty.getDingTalkWebHookDevToken().isEmpty()) {
                    throw new IllegalAccessException("预警模块配置文件初始化异常,项目未指定 dingTalkWebHookDevToken , 钉钉预警无法初始化回调信息");
                }
                dingTalkWebHook = alarmNoticeProperty.getDingTalkWebHookDevToken();
            } else if (active.contains("prod")) {
                if (null == alarmNoticeProperty.getDingTalkWebHookProdToken() ||
                        alarmNoticeProperty.getDingTalkWebHookProdToken().isEmpty()) {
                    throw new IllegalAccessException("预警模块配置文件初始化异常,项目未指定 dingTalkWebHookProdToken , 钉钉预警无法初始化回调信息");
                }
                dingTalkWebHook = alarmNoticeProperty.getDingTalkWebHookProdToken();
            } else {
                throw new IllegalAccessException("预警模块配置文件初始化异常,项目未指定合法的 spring.profiles.active , 钉钉预警无法初始化回调信息");
            }
            alarmNoticeProperty.setDingTalkWebHook(dingTalkWebHook);
        } else {
            throw new IllegalAccessException("预警模块配置文件初始化异常,项目未指定 spring.profiles.active , 钉钉预警无法初始化回调信息");
        }
        alarmDingTalkWebHookTokenManager.loadToken();
        return new AlarmNoticeManage(alarmNoticeProperty);
    }

    @EventListener
    public void envListener(EnvironmentChangeEvent event) {
        log.info("envListener {}", event.getSource());
    }

}
