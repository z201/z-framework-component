package cn.z201.cloud.api.annotation;

import cn.z201.cloud.api.selector.MdcLogImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author z201.coding@gmail.com
 * 开启对mdc 日志对支持。
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MdcLogImportSelector.class})
public @interface EnableMdcLogging {

    /**
     * 是否启用
     *
     * @return
     */
    boolean enable() default false;

}
