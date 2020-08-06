package cn.z201.cloud.auth.enums;

/**
 * @author z201.coding@gmail.com
 **/
public enum PlatformUserMessageDetailsEnum {
    /**
     * 友盟
     */
    U_MENG(1),

    /**
     * 微信小程序
     */
    WE_CHAT_MINI_PROGRAM(2),

    /**
     * 微信公众号
     */
    WE_CHAT_PUBLIC_ACCOUNT(3);

    /**
     * 状态
     */
    private int value;

    PlatformUserMessageDetailsEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
