package cn.z201.cloud.encrypt.core.exception;

/**
 * <p>解密数据失败异常</p>
 */
public class BodyFailException extends RuntimeException {

    public BodyFailException() {
        super("Decrypting data failed. (解密数据失败)");
    }

    public BodyFailException(String message) {
        super(message);
    }
}