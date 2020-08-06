package cn.z201.cloud.auth.manager;

import org.springframework.web.client.RestTemplate;

/**
 * @author z201.coding@gmail.com
 **/
public interface RestTemplateManagerI {

    RestTemplate defHttps();
}
