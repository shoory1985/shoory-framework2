package com.shoory.framework.starter.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

@ApiModel
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRequest {

	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR_UNKNOWN = "ERROR_UNKNOWN";
	public static final String ERROR_METHOD_NOT_FOUND = "ERROR_METHOD_NOT_FOUND";
	public static final String ERROR_INTERNAL = "ERROR_INTERNAL";
	public static final String ERROR_INVALID_PARAMETERS = "ERROR_INVALID_PARAMETERS";

	@ApiModelProperty(value = "语言", notes = "默认zh_CN", example = "zh_CN")
	private String lang = "zh_CN";
	

	@ApiModelProperty(value = "地址", hidden = true)
	private String _clientAddress;	
}
