package cn.z201.cloud.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBindingInfoDto {

    List<PlatformAccountBindingInfoDto> accountBindingInfoList;
}
