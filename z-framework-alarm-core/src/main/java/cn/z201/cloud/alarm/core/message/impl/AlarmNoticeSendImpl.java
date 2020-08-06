package cn.z201.cloud.alarm.core.message.impl;


import cn.z201.cloud.alarm.core.client.SimpleHttpClient;
import cn.z201.cloud.alarm.core.dto.*;
import cn.z201.cloud.alarm.core.manager.AlarmDingTalkWebHookTokenManager;
import cn.z201.cloud.alarm.core.message.AlarmNoticeSendI;
import cn.z201.cloud.alarm.core.property.AlarmNoticeProperty;
import com.google.gson.Gson;
import cn.z201.cloud.alarm.core.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author z201.coding@gmail.com
 **/
@Service
public class AlarmNoticeSendImpl implements AlarmNoticeSendI {

    private static final Logger log = LoggerFactory.getLogger(AlarmNoticeSendImpl.class);

    private static final String ULR = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    private AlarmDingTalkWebHookTokenManager alarmDingTalkWebHookTokenManager;

    private AlarmNoticeProperty alarmNoticeProperty;

    @Autowired
    public AlarmNoticeSendImpl(AlarmNoticeProperty alarmNoticeProperty, AlarmDingTalkWebHookTokenManager alarmDingTalkWebHookTokenManager) {
        this.alarmNoticeProperty = alarmNoticeProperty;
        this.alarmDingTalkWebHookTokenManager = alarmDingTalkWebHookTokenManager;
    }

    @Override
    public void send(ServiceNotice serviceNotice, String... blamedFor) {
        if (blamedFor != null) {
            DingDingNotice dingDingNotice = new DingDingNotice(
                    serviceNotice.getTitle(),
                    serviceNotice.createText(),
                    blamedFor);
            Gson gson = new Gson();
            log.info(gson.toJson(dingDingNotice));
            try {
                String url = String.format(ULR, alarmDingTalkWebHookTokenManager.availableToken());
                DingDingResult result = SimpleHttpClient.post(url, dingDingNotice,
                        DingDingResult.class);
                log.info("result {} ", result);
            } catch (Exception e) {
                log.info("异常预警失败 {}", serviceNotice);
            }
        } else {
            log.info("无法进行钉钉通知，不存在背锅侠");
        }
    }

    @Override
    public void send(ServiceNotice serviceNotice, String blamedFor) {
        send(serviceNotice, new String[]{blamedFor});
    }

    @Override
    public void sendNotice(Throwable throwable, String extMessage, String blamedFor) {
        sendNotice(throwable, extMessage, new String[]{blamedFor});
    }

    @Override
    public void sendNotice(Throwable throwable, String extMessage, String... blamedFor) {
        if (blamedFor != null) {
            ExceptionNotice exceptionNotice = new ExceptionNotice(throwable,
                    new String[]{extMessage});
            exceptionNotice.setProject(alarmNoticeProperty.getProjectName());
            Gson gson = new Gson();
            ExceptionTxtNotice exceptionTxtNotice = new ExceptionTxtNotice(exceptionNotice.createText(),
                    blamedFor);
            log.info(gson.toJson(exceptionTxtNotice));
            try {
                String url = String.format(ULR, alarmDingTalkWebHookTokenManager.availableToken());
                DingDingResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                        DingDingResult.class);
                log.info("result {} ", result);
            } catch (Exception e) {
                log.info("异常预警失败 {}", exceptionNotice);
            }
        } else {
            log.info("无法进行钉钉通知，不存在背锅侠");
        }
    }

    @Override
    public void sendNotice(Throwable throwable, String appTraceId, String extMessage, String blamedFor) {
        sendNotice(throwable, appTraceId, extMessage, new String[]{blamedFor});
    }

    @Override
    public void sendNotice(Throwable throwable, String appTraceId, String extMessage, String... blamedFor) {
        if (blamedFor != null) {
            ExceptionNotice exceptionNotice = new ExceptionNotice(throwable, appTraceId,
                    new String[]{extMessage});
            exceptionNotice.setProject(alarmNoticeProperty.getProjectName());
            Gson gson = new Gson();
            ExceptionTxtNotice exceptionTxtNotice = new ExceptionTxtNotice(exceptionNotice.createText(),
                    blamedFor);
            if (log.isDebugEnabled()) {
                log.debug(gson.toJson(exceptionTxtNotice));
            }
            try {
                String url = String.format(ULR, alarmDingTalkWebHookTokenManager.availableToken());
                DingDingResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                        DingDingResult.class);
                log.info("result {} ", result);
            } catch (Exception e) {
                log.info("异常预警失败 {} {}", exceptionNotice , e);
            }
        } else {
            log.info("无法进行钉钉通知，不存在背锅侠");
        }
    }

}
