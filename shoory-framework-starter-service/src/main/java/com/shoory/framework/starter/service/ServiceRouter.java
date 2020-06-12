package com.shoory.framework.starter.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.api.BizException;
import com.shoory.framework.starter.api.request.BaseRequest;
import com.shoory.framework.starter.api.response.BaseResponse;
import com.shoory.framework.starter.utils.PojoUtils;

@Component
public class ServiceRouter {
	private static final Logger logger = LoggerFactory.getLogger(ServiceRouter.class);
	@Autowired
	private PojoUtils pojoUtils;
	@Autowired
	private I18nComponent i18nComponent;

	public BaseResponse dispatch(BaseService service, BaseRequest request) {
		try {
			BaseResponse response = null;
			
			try {
				// 验证入参（验证失败则抛出异常）
				pojoUtils.validate(request);
				
				// 乐观锁重试
				int numAttempts = 0;
				int maxRetries = 2;
				for (numAttempts = 0; numAttempts <= maxRetries; numAttempts++) {
					try {
						response = service.invoke(request);
						response.setCode("SUCCESS");
						break;
					} catch (Throwable ex) { // 乐观锁
						if (ex.getClass().getSimpleName().indexOf("PessimisticLockingFailureExceptionException") >= 0) {
							continue;
						} else {
							throw ex;
						}
					}
				}
			} catch (BizException be) {
				response = new BaseResponse();
				response.setCode(be.getMessage());
			} catch (Throwable e) {
				response = new BaseResponse();
				response.setCode(BaseRequest.ERROR_INTERNAL);
				response.setMessage(e.getMessage());
				e.printStackTrace();
			}

			//调用完成
			
			//i18n
			if (response.getMessage() == null) {
				response.setMessage(Optional.ofNullable(i18nComponent.getMessage(response.getCode(), request.getLang()))
						.orElse(response.getCode()));
			}
			
			return response;
		} catch (Exception e) {
			logger.info(e.getMessage());
			BaseResponse response = new BaseResponse();
			e.printStackTrace();
			response.setCode(BaseRequest.ERROR_INTERNAL);
			response.setMessage(e.getMessage());
			return response;
		}
	}
}
