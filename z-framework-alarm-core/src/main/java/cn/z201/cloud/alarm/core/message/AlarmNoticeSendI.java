package cn.z201.cloud.alarm.core.message;


import cn.z201.cloud.alarm.core.dto.ServiceNotice;

/**
 * @author z201.coding@gmail.com
 **/
public interface AlarmNoticeSendI {

    void send(ServiceNotice serviceNotice, String... blamedFor);

    void send(ServiceNotice serviceNotice, String blamedFor);

    void sendNotice(Throwable throwable, String extMessage, String blamedFor);

    void sendNotice(Throwable throwable, String extMessage, String... blamedFor);

    void sendNotice(Throwable throwable, String appTraceId, String extMessage, String blamedFor);

    void sendNotice(Throwable throwable, String appTraceId, String extMessage, String... blamedFor);

}
