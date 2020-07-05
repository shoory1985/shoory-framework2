package com.shoory.framework.starter.jwt;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.FieldSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shoory.framework.starter.api.BizException;
import com.shoory.framework.starter.api.annotation.Authorized;
import com.shoory.framework.starter.api.request.AuthorizedRequest;
import com.shoory.framework.starter.api.response.AuthorizedResponse;
import com.shoory.framework.starter.api.response.BaseResponse;

@Aspect
@Component
public class JwtAspect {
	private Logger logger = LoggerFactory.getLogger(JwtAspect.class);
	@Autowired
	private JwtUtils jwtUtils;

	/**
	 * 环绕通知
	 * 
	 * @param joinPoint
	 * @return 返回被切入的方法的返回值
	 * @throws Throwable
	 */
	@Around("@annotation(com.shoory.framework.starter.api.annotation.Authorized)")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 校验JWT
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String token = attributes.getRequest().getHeader("Authorization");
		if (StringUtils.isBlank(token)) {
			throw new BizException(AuthorizedResponse.ERROR_ACCESS_TOKEN_MISSED);
		}

		jwtUtils.checkAccessToken(token);

		//注入AuthorizedRequest
		Arrays.stream(joinPoint.getArgs())
			.findFirst()
			.filter(parameter -> parameter instanceof AuthorizedRequest)
			.ifPresent(parameter -> {
				DecodedJWT jwt = JWT.decode(token);
				if (StringUtils.isBlank(jwt.getSubject())) {
					throw new BizException(AuthorizedResponse.ERROR_INVALID_CREDENTIAL);
				}

				AuthorizedRequest ar = (AuthorizedRequest) parameter;
				ar.set_credential(jwt.getSubject());
				ar.set_scene(jwt.getAudience().stream().collect(Collectors.joining(",")));
			});

		Object retVal = joinPoint.proceed();// 这个方法会执行被切入的运行时方法，然后获取返回值，所以不会运行两次

		return retVal;
	}
}
