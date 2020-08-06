package cn.z201.cloud.auth.config;

import cn.z201.cloud.auth.manager.AuthManagerI;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.synchronizedMap;

/**
 * @author z201.coding@gmail.com
 **/
public class AuthManagerContext {

    private Map<Integer, Class> handlerMap = synchronizedMap(new HashMap<>());

    public AuthManagerContext(Map<Integer, Class> handlerMap) {
        this.handlerMap.clear();
        this.handlerMap.putAll(handlerMap);
    }

    public AuthManagerI getInstance(Integer type) {
        Class clazz = handlerMap.get(type);
        if (clazz == null) {
            throw new IllegalArgumentException("not found handler for type: " + type);
        }
        return (AuthManagerI) BeanTool.getBean(clazz);
    }

}
