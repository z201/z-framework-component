package cn.z201.cloud.encrypt.core.dto;

import cn.z201.cloud.encrypt.core.utils.AESEncryptOperator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Base64;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpBodyEncryptBodyDto {

    Long timestamp;

    String data;

    String sign;

    public void init() {
        this.timestamp = System.currentTimeMillis();
        this.sign = DigestUtils.md5Hex((timestamp + data));
        this.data = Base64.getEncoder().encodeToString(AESEncryptOperator.encrypt(data).getBytes());
    }

}
