package cn.z201.cloud.api.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author z201.coding@gmail.com
 **/
public class MdcLogImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                "cn.z201.cloud.api.mdc.MdcApiLogAutoConfig",
                "cn.z201.cloud.api.mdc.MdcFeignInterceptorConfig"
        };
    }
}
