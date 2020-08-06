package cn.z201.cloud.gateway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@NoArgsConstructor
public class Result {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;

    public Result(int code, String message) {
        this.code = code;
        msg = message;
    }

    public static Result fail(){
        return new Result(Code.ERROR.getCode(),Code.ERROR.getMsg());
    }

    public static Result fail(String msg){
        return new Result(Code.ERROR.getCode(),msg);
    }

    public static Result success() {
        return new Result(Code.SUCCESS.getCode(),Code.SUCCESS.getMsg());
    }

    /**
     * 拓展状态码
     */
    public enum Code {
        // 正确的
        SUCCESS(20000, "成功"),
        // 登录超时
        LOGIN_TIMEOUT(20002, "登录超时"),
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
            // TODO Auto-generated method stub
            return this.value;
        }

        public String getMsg() {
            // TODO Auto-generated method stub
            return this.msg = msg;
        }
    }
}
