package cn.z201.cloud.encrypt.core.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author z201.coding@gmail.com
 **/
public class DecryptHttpBodySelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[] {
                "cn.z201.cloud.encrypt.core.HttpBodyEncryptConfig"
        };
    }
}
