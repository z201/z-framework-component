package cn.z201.cloud.auth.enums;

/**
 * 内部类返回
 * @author wangzhengnian
 *
 */
public enum ResponseCodeEnum {

	SUCCESS(200, "成功"),

	FAIL(400, "业务失败"),

	ERROR(500, "系统异常");

	private int code;

	private String message;

	private ResponseCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
