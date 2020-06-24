package com.shoory.framework.starter.api.request;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shoory.framework.starter.api.response.BaseResponse;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseSessionRequest extends BaseRequest {

	@NotBlank(message = BaseResponse.ERROR_NO_AUTHORZIATION)
	private String _credential;
}
