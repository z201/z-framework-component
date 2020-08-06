package cn.z201.cloud.auth.annotation;

import java.lang.annotation.*;

/**
 * @author z201.coding@gmail.com
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AuthManagerHandler {

    int type() default 0;
}
