package cn.z201.cloud.auth.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyImageDto {

    String code;

    ByteArrayOutputStream bos;

}
