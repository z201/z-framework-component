package cn.z201.cloud.auth.enums;

/**
 * @author z201.coding@gmail.com
 **/
public enum PlatformAccountBindingInfoEnum {

    /**
     * 账号绑定信息
     */
    PHONE(0, "手机号"),
    WX(1, "微信"),
    WB(2, "微博"),
    QQ(3, "QQ"),
    PWD(99, "设置密码");

    /**
     * 绑定编号
     */
    private final int code;

    /**
     * 名称
     */
    private final String name;

    PlatformAccountBindingInfoEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
