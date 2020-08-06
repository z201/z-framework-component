package cn.z201.cloud.alarm.core.dto;

import lombok.Data;

@Data
public class DingDingNotice {

    private DingDingText markdown;
    private DingDingAt at;
    private String msgtype = "markdown";

    public DingDingNotice(String title, String text, String... at) {
        this.at = new DingDingAt(at);
        this.markdown = new DingDingText(title, text + "\n" + atStr(at));
    }

    private String atStr(String... at) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < at.length; i++) {
            stringBuilder.append("@" + at[i]);
        }
        return stringBuilder.toString();
    }

}
