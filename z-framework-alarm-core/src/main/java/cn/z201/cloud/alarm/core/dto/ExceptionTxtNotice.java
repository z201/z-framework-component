package cn.z201.cloud.alarm.core.dto;

import lombok.Data;

@Data
public class ExceptionTxtNotice {

    private DingDingTxtText text;
    private DingDingAt at;
    private String msgtype = "text";

    public ExceptionTxtNotice(DingDingTxtText text, DingDingAt at, String msgtype) {
        super();
        this.text = text;
        this.at = at;
        this.msgtype = msgtype;
    }

    public ExceptionTxtNotice(DingDingTxtText text, DingDingAt at) {
        this.text = text;
        this.at = at;
    }

    public ExceptionTxtNotice(String text, String... at) {
        this.text = new DingDingTxtText(text);
        this.at = new DingDingAt(at);
    }
}
