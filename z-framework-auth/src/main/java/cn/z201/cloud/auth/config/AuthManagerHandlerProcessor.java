package cn.z201.cloud.auth.config;

import cn.z201.cloud.auth.annotation.AuthManagerHandler;
import cn.z201.cloud.auth.utils.ClassScaner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@Component
public class AuthManagerHandlerProcessor implements BeanFactoryPostProcessor {

    private static final String HANDLER_PACKAGE = "com.vlink.cloud.auth.manager.impl";

    /**
     * 扫描@AuthManagerHandler，初始化HandlerContext，将其注册到spring容器
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<Integer, Class> handlerMap = new HashMap<>();
        ClassScaner.scan(HANDLER_PACKAGE, AuthManagerHandler.class).forEach(clazz -> {
            // 获取注解中的类型值
            Integer type = clazz.getAnnotation(AuthManagerHandler.class).type();
            // 将注解中的类型值作为key，对应的类作为value，保存在Map中
            handlerMap.put(type, clazz);
        });
        // 初始化HandlerContext，将其注册到spring容器中
        AuthManagerContext context = new AuthManagerContext(handlerMap);
        beanFactory.registerSingleton(AuthManagerContext.class.getName(), context);
    }

}
