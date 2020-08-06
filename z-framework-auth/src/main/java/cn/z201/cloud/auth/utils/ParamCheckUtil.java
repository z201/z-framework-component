package cn.z201.cloud.auth.utils;


import cn.z201.cloud.auth.exception.AuthException;
import cn.z201.cloud.auth.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数校验
 *
 * @author wangzhengnian
 */
@Slf4j
public class ParamCheckUtil {
    /**
     * 检查所有参数中是否有空值
     *
     * @param args
     * @return
     */
    public static boolean checkArgsIsNotNull(Object... args) {
        try {
            if (null == args || args.length <= 0) {
                return false;
            }
            for (Object object : args) {
                if (object == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            log.error("校验参数失败", e);
            return false;
        }
    }

    /**
     * 检查所有参数中是否有空值
     * 且判断所有数值型不小于0
     *
     * @param args
     * @return
     */
    public static boolean checkArgsIsNotNullAndNumberIsNotZero(Object... args) {
        try {
            if (null == args || args.length <= 0) {
                return false;
            }
            for (Object object : args) {
                if (object == null) {
                    return false;
                }
                if (object instanceof Number || object instanceof BigDecimal) {
                    if (Double.valueOf(object.toString()) <= 0) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("校验参数失败", e);
            return false;
        }
    }

    public static void checkArgsIsNotNullAndNumberIsNotZeroAfterThrowException(Object... args) {
        if (!ParamCheckUtil.checkArgsIsNotNullAndNumberIsNotZero(args)) {
            log.warn("请求参数不合法，请将参数填写完整");
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "请将参数填写完整");
        }
    }

    /**
     * 检查手机号
     *
     * @param phone
     */
    public static void checkPhoneAfterThrowException(String phone) {
        if (Strings.isEmpty(phone)) {
            throw new AuthException(Result.Code.NO_UPDATE.getCode(), "手机号不合法");
        }
        String regex = "^((1[0-9])([0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        if (!isMatch) {
            log.warn("手机号格式不正确,请换一个 {} ", phone);
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(), "请求参数不合法，手机号格式不正确。请换一个。");
        }
    }

    public static void checkAccountSmsCodeAfterThrowException(String smsCode, String smsCodeCache) {
        if (!smsCode.equals(smsCodeCache)) {
            throw new AuthException(Result.Code.PARAMETER_ERROR.getCode(),"验证码填写错误");
        }
    }

    /**
     * 检查所有参数中是否有空值
     * 且判断所有数值型不小于0
     * 且检查列表长度>0
     *
     * @param args
     * @return
     */
    public static boolean checkArgsIsNotNullAndNumberIsNotZeroAndListIsNotEmpty(Object... args) {
        try {
            if (null == args || args.length <= 0) {
                return false;
            }
            for (Object object : args) {
                if (object == null) {
                    return false;
                }
                if (object instanceof Number || object instanceof BigDecimal) {
                    if (Double.valueOf(object.toString()) <= 0) {
                        return false;
                    }
                }
                if (object instanceof Collection) {
                    if (((Collection<?>) object).size() <= 0) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error("校验参数失败", e);
            return false;
        }
    }

}
