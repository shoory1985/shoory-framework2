package com.shoory.framework.starter.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR_UNKNOWN = "ERROR_UNKNOWN";
	public static final String ERROR_INTERNAL = "ERROR_INTERNAL";
	public static final String ERROR_INVALID_PARAMETERS = "ERROR_INVALID_PARAMETERS";
	public static final String ERROR_OPERATION_FORBIDDEN = "ERROR_OPERATION_FORBIDDEN";
	
	@ApiModelProperty(value = "响应代码", example = "SUCCESS")
	private String code = "SUCCESS";

	@ApiModelProperty(value = "响应消息", example = "")
	private String message;
	
	@JsonIgnore
	public boolean isSuccess() {
		return SUCCESS.equals(this.code);
	}
}
