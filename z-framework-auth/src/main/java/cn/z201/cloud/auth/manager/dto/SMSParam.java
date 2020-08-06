package cn.z201.cloud.auth.manager.dto;

import lombok.Data;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
@Data
public class SMSParam {

    //手机号
    private String mobile;

    //批量手机号
    private List<String> mobiles;

    //短信内容
    private String content;

    //验证码
    private String code;

    //验证码类型
    private String codeType;
}