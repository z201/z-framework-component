package cn.z201.cloud.encrypt.core.annotation;

import java.lang.annotation.*;

/**
 * @author z201.coding@gmail.com
 **/
@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DecryptBody {

    String other() default "";

}
