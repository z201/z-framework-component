package cn.z201.cloud.auth.enums;

/**
 * @author z201.coding@gmail.com
 **/
public enum PlatformAccountOauthEnum {

    /**
     * 客户端授权来源
     */
    APP_WE_CHAT(1, "微信APP客户端"),
    MINI_PROGRAM_WE_CHAT(2, "微信小程序"),
    WAP_WE_CHAT(3, "微信公众号");

    private Integer code;
    private String desc;

    PlatformAccountOauthEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static PlatformAccountOauthEnum findPlatformAccountOauthEnumByCode(Integer code) {
        for (PlatformAccountOauthEnum value : PlatformAccountOauthEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

}
