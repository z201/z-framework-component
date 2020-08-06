package cn.z201.cloud.auth.dto;


import java.io.Serializable;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 状态码
     */
    private int code = Code.SUCCESS.value;

    /**
     * 数据对象
     */
    private Object resp;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 封装结果对象
     *
     * @param code 状态码
     * @param msg  状态信息
     * @param resp 待封装结果对象
     */
    private Result(Code code, String msg, Object resp) {
        this.code = code.value;
        this.resp = resp;
        this.msg = msg;
    }

    private Result(int code, String msg, Object resp) {
        this.code = code;
        this.resp = resp;
        this.msg = msg;
    }

    public static Result success(Object value) {
        return new Result(Code.SUCCESS, null, value);
    }

    public static Result success(Map<String, Object> data) {
        return new Result(Code.SUCCESS, null, data);
    }

    public static Result success() {
        return new Result(Code.SUCCESS, Code.SUCCESS.msg, null);
    }

    public static Result error(int code, String msg) {
        return new Result(code, msg, null);
    }

    public int getCode() {
        return code;
    }

    public Object getResp() {
        return resp;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 拓展状态码
     */
    public enum Code {
        // 正确的
        SUCCESS(20000, "成功"),
        PARAMETER_ERROR(20006, "请求参数错误"),
        NO_UPDATE(20009, "操作失败"),
        ERROR(50001, "网络开小差，请稍后尝试");

        /**
         *
         */

        /**
         * 状态码值
         */
        private int value;
        private String msg;

        Code(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        int getCode(Code code) {
            return value;
        }

        public int getcode() {
            return this.value;

        }

        public int getCode() {
            return this.value;
        }

        public String getMsg() {
            return this.msg = msg;
        }

    }
}
