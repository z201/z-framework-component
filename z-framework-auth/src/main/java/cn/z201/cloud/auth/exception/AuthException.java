package cn.z201.cloud.auth.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthException extends RuntimeException {

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message = "";

    /**
     * 异常扩展信息
     */
    private String extMessage = "";

    public AuthException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.code = errorCode;
        this.message = errorMsg;
    }

    public AuthException(int errorCode, String errorMsg, String extMessage) {
        super(errorMsg);
        this.code = errorCode;
        this.message = errorMsg;
        this.extMessage = extMessage;
    }
}
