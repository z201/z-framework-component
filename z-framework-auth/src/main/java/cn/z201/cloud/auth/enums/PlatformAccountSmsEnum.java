package cn.z201.cloud.auth.enums;

import cn.z201.cloud.auth.exception.AuthException;
import cn.z201.cloud.auth.dto.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
public enum PlatformAccountSmsEnum {

    LOGIN(1, "登录短信"),
    FORGET(2, "找回密码短信"),
    RESET_PWD(3, "设置密码"),
    CHECK_PHONE(4, "验证手机号");

    private int code;
    private String desc;

    PlatformAccountSmsEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 获取并且检查code是否有效，若有效则返回。没有则直接异常
     * @param code
     * @return
     */
    public static PlatformAccountSmsEnum findAndCheckCodeAfterThrowException(Integer code) {
        for (PlatformAccountSmsEnum platformAccountSmsEnum : PlatformAccountSmsEnum.values()) {
            if (platformAccountSmsEnum.getCode() == code) {
                return platformAccountSmsEnum;
            }
        }
        log.warn("短信type不合法，请将参数填写完整");
        throw new AuthException(Result.Code.NO_UPDATE.getCode(), "请将参数填写完整");
    }
}
