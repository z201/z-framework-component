package cn.z201.cloud.encrypt.core.advice;



import cn.z201.cloud.encrypt.core.annotation.DecryptBody;
import cn.z201.cloud.encrypt.core.bean.DecryptHttpInputMessage;
import cn.z201.cloud.encrypt.core.dto.HttpBodyEncryptBodyDto;
import cn.z201.cloud.encrypt.core.exception.BodyFailException;
import cn.z201.cloud.encrypt.core.property.HttpBodyEncryptProperty;
import cn.z201.cloud.encrypt.core.utils.AESEncryptOperator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Base64;

/**
 * 本类只对控制器参数中含有<strong>{@link org.springframework.web.bind.annotation.RequestBody}</strong>
 *
 * @see RequestBodyAdvice
 */
@Order(1)
@ControllerAdvice
@Slf4j
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Autowired
    HttpBodyEncryptProperty httpBodyEncryptProperty;

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        if (!httpBodyEncryptProperty.getEnable()) {
            return false;
        }
        Annotation[] annotations = methodParameter.getDeclaringClass().getAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof DecryptBody) {
                    return true;
                }
            }
        }
        return methodParameter.getMethod().isAnnotationPresent(DecryptBody.class);

    }

    @Override
    public Object handleEmptyBody(Object body,
                                  HttpInputMessage inputMessage,
                                  MethodParameter parameter,
                                  Type targetType,
                                  Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                           MethodParameter parameter,
                                           Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (inputMessage.getBody() == null) {
            return inputMessage;
        }
        String body;
        try {
            body = IOUtils.toString(inputMessage.getBody(), "UTF-8");
        } catch (Exception e) {
            log.error("Unable to get request body data , " +
                    "please check if the sending data body or request method is in compliance with the specification. " +
                    "(无法获取请求正文数据，请检查发送数据体或请求方法是否符合规范。)");
            throw new BodyFailException("Unable to get request body data," +
                    " please check if the sending data body or request method is in compliance with the specification." +
                    " (无法获取请求正文数据，请检查发送数据体或请求方法是否符合规范。)");
        }
        if (body == null || isNullOrEmpty(body)) {
            log.error("The request body is NULL or an empty string," +
                    " so the decryption failed ." +
                    " (请求正文为NULL或为空字符串，因此解密失败。");
            throw new BodyFailException("The request body is NULL or an empty string," +
                    " so the decryption failed." +
                    " 请求正文为NULL或为空字符串，因此解密失败");
        }
        if (log.isDebugEnabled()) {
            log.debug("old body {}", body);
        }
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        HttpBodyEncryptBodyDto httpBodyEncryptBodyDto = gson.fromJson(body, HttpBodyEncryptBodyDto.class);
        if (null == httpBodyEncryptBodyDto ||
                null == httpBodyEncryptBodyDto.getData() ||
                null == httpBodyEncryptBodyDto.getSign() ||
                null == httpBodyEncryptBodyDto.getTimestamp()) {
            log.info("body error {}", body);
            throw new BodyFailException("The request body is illegal ," +
                    " so the decryption failed." +
                    " 请求体不合法，因此解密失败");
        }
        Long timestamp = httpBodyEncryptBodyDto.getTimestamp();
        if (log.isDebugEnabled()) {
            log.debug("timestamp {}", timestamp);
        }
        String sign = httpBodyEncryptBodyDto.getSign();
        if (log.isDebugEnabled()) {
            log.debug("old sign {}", sign);
        }
        String data = new String(Base64.getDecoder().decode(httpBodyEncryptBodyDto.getData()));
        body = AESEncryptOperator.decrypt(data);
        if (log.isDebugEnabled()) {
            log.debug("new  body {}", body);
        }
        if (!validate(body)) {
            log.error("data error 请求体错误, data 不符合json格式 old {} ", body);
            throw new BodyFailException("data error 请求体错误,data 不符合json格式");
        }
        String checkSign = DigestUtils.md5Hex((timestamp + body));
        if (log.isDebugEnabled()) {
            log.debug("checkSign  {}", checkSign);
        }
        if (!checkSign.equals(sign)) {
            log.error("checkSign error Decryption error," +
                    " (解密错误，请检查源数据的验证签发是否正确");
            throw new BodyFailException("Decryption error," +
                    " (解密错误，请检查源数据的验证签发是否正确");
        }
        if (body == null) {
            log.error("body is null Decryption error, " +
                    "(解密错误，请检查源数据的验证签发是否正确");
            throw new BodyFailException("Decryption error," +
                    "(解密错误，请检查选择的源数据的加密方式是否正确。");
        }
        try {
            InputStream inputStream = IOUtils.toInputStream(body, "UTF-8");
            return new DecryptHttpInputMessage(inputStream, inputMessage.getHeaders());
        } catch (Exception e) {
            log.error("The string is converted to a stream format exception. " +
                    "(字符串转换成流格式异常，请检查编码等格式是否正确");
            throw new BodyFailException("The string is converted to a stream format exception ." +
                    "(字符串转换成流格式异常，请检查编码等格式是否正确");
        }
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean validate(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;
    }


}
