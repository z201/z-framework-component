package cn.z201.cloud.alarm.core.manager;

import cn.z201.cloud.alarm.core.property.AlarmNoticeProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.synchronizedMap;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
@Service
public class AlarmDingTalkWebHookTokenManager {

    @Autowired
    AlarmNoticeProperty alarmNoticeProperty;

    /**
     * 计数器
     */
    private AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, String> tokenMaps = synchronizedMap(new LinkedHashMap<>());

    public void loadToken() {
        log.info("init alarmDingTalkWebHookTokenManager loadToken");
        tokenMaps.clear();
        if (null != alarmNoticeProperty.getDingTalkWebHook() && !alarmNoticeProperty.getDingTalkWebHook().isEmpty()) {
            for (int i = 0; i < alarmNoticeProperty.getDingTalkWebHook().size(); i++) {
                tokenMaps.put(i, alarmNoticeProperty.getDingTalkWebHook().get(i));
            }
        }
    }

    public String availableToken() {
        int length = tokenMaps.size();
        int index = counter.incrementAndGet();
        return tokenMaps.get(index % length);
    }

}
