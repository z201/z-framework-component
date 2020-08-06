package cn.z201.cloud.api.feign;

import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
public class FeignExceptionConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserErrorDecoder();
    }

    /**
     * 重新实现feign的异常处理，捕捉restful接口返回的json格式的异常信息
     */
    public class UserErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            Exception exception = null;
            try {
                String json = Util.toString(response.body().asReader());
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
            return exception = feign.FeignException.errorStatus(methodKey, response);
        }
    }
}
