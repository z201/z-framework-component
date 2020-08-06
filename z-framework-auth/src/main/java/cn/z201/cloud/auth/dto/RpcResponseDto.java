package cn.z201.cloud.auth.dto;

import cn.z201.cloud.auth.enums.ResponseCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: lx
 * @Date: 2019/5/22
 */
@Data
public class RpcResponseDto<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private T data;

    /**
     * 二进制返回
     */
    private byte[] dataBytes;

    public RpcResponseDto(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public RpcResponseDto(ResponseCodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

    public static RpcResponseDto successResponseDto() {
        return new RpcResponseDto(ResponseCodeEnum.SUCCESS);
    }

    public static RpcResponseDto successResponseDto(byte[] dataBytrs) {
        RpcResponseDto responseDto = successResponseDto();
        responseDto.setDataBytes(dataBytrs);
        return responseDto;
    }

    public static <T> RpcResponseDto<T> successResponseDto(T data) {
        RpcResponseDto<T> responseDto = successResponseDto();
        responseDto.setData(data);
        return responseDto;
    }

    public static RpcResponseDto errorResponseDto() {
        return new RpcResponseDto(ResponseCodeEnum.ERROR);
    }

    public static RpcResponseDto errorResponseDto(String message) {
        RpcResponseDto responseDto = errorResponseDto();
        responseDto.setMessage(message);
        return responseDto;
    }

    public static RpcResponseDto failResponseDto() {
        return new RpcResponseDto(ResponseCodeEnum.FAIL);
    }

    public static RpcResponseDto failResponseDto(String message) {
        RpcResponseDto responseDto = failResponseDto();
        responseDto.setMessage(message);
        return responseDto;
    }

    public RpcResponseDto() {
        super();
    }

    public boolean success() {
        if (getCode() != ResponseCodeEnum.SUCCESS.getCode()) {
            return false;
        }
        return true;
    }

}
