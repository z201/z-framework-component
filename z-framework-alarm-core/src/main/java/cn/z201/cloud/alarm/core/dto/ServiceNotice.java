package cn.z201.cloud.alarm.core.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * @author z201.coding@gmail.com
 **/
@Data
public class ServiceNotice {

    String title;

    String appName;

    String serverId;

    Long timestamp;

    String profiles;

    public String createText() {
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#### ").append(title).append("\n ");
        stringBuilder.append(">  ").append(time).append("\n ");
        stringBuilder.append(">  ").append(appName).append("\n ");
        stringBuilder.append(">  ").append(serverId).append("\n ");
        stringBuilder.append(">  profiles ").append(profiles).append("\n ");
        return stringBuilder.toString();
    }

}
