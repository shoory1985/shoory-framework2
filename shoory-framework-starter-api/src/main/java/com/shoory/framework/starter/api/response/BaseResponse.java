package com.shoory.framework.starter.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
	@ApiModelProperty(value = "响应代码", example = "SUCCESS")
	private String code = "ERROR_UNKNOWN";

	@ApiModelProperty(value = "响应消息", example = "")
	private String message;
}
