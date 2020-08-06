package cn.z201.cloud.alarm.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DingDingResult {

	private int errcode;
	private String errmsg;

}
