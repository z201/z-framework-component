package cn.z201.cloud.alarm.core.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@ConfigurationProperties(prefix = "alarm")
@RefreshScope
@Data
public class AlarmNoticeProperty {
    /**
     * 是否开启异常通知
     */
    private Boolean openNotice;

    /**
     * 异常工程名
     */
    @Value("${spring.application.name?:['未指定项目名称']}")
    private String projectName;

    /**
     * 默认通知人，当异常通知找不到背锅侠时，就用默认背锅侠
     */
    private String defaultNotice;

    /**
     * 钉钉webHook 开发环境
     */
    private List<String> dingTalkWebHookDevToken;

    /**
     * 钉钉webHook 正式环境
     */
    private List<String> dingTalkWebHookProdToken;

    /**
     * 钉钉钉webHook
     */
    private List<String> dingTalkWebHook;

    /**
     * 令牌每分钟限流
     */
    private Integer tokenLimitPerMinute;

    /**
     * 发送钉钉异常通知给谁
     */
    private Map<String, String> dingTalkNotice;

}
