package com.shoory.framework.starter.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.shoory.framework.starter.api.request.AuthorizedRequest;
import com.shoory.framework.starter.api.response.BaseResponse;

@Aspect
@Component
public class JwtAuthorizationAspect {
	@Value("${jwt.secret:}")
	private String JWT_SECRET;
	
	private Logger logger = LoggerFactory.getLogger(JwtAuthorizationAspect.class);
	private JWTVerifier jwtVerifer = null;

	/**
	 * 环绕通知
	 * 
	 * @param joinPoint
	 * @return 返回被切入的方法的返回值
	 * @throws Throwable
	 */
	@Around("@annotation(com.shoory.framework.starter.api.annotation.JwtAuthorization)")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 校验JWT
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		String token = attributes.getRequest().getHeader("Authorization");
		if (StringUtils.isBlank(token)) {
			throw new BizException(BaseResponse.ERROR_ACCESS_TOKEN_MISSED);
		}

		try {
			if (jwtVerifer == null) {
				jwtVerifer = JWT.require(Algorithm.HMAC256(JWT_SECRET)).build();
			}
			jwtVerifer.verify(token);
		} catch (TokenExpiredException e) {
			throw new BizException(BaseResponse.ERROR_ACCESS_TOKEN_EXPIRED);
		} catch (Exception e) {
			throw new BizException(BaseResponse.ERROR_INVALID_ACCESS_TOKEN);
		}

		// 注入AuthorizedRequest
		Arrays.stream(joinPoint.getArgs())
			.findFirst()
			.filter(parameter -> parameter instanceof AuthorizedRequest)
			.ifPresent(parameter -> {
				DecodedJWT jwt = JWT.decode(token);
				if (StringUtils.isBlank(jwt.getSubject())) {
					throw new BizException(BaseResponse.ERROR_INVALID_CREDENTIAL);
				}

				AuthorizedRequest ar = (AuthorizedRequest) parameter;
				ar.set_userId(Long.valueOf(jwt.getSubject()));
				ar.set_scene(jwt.getAudience().stream().collect(Collectors.joining(",")));
			});

		Object retVal = joinPoint.proceed();// 这个方法会执行被切入的运行时方法，然后获取返回值，所以不会运行两次

		return retVal;
	}
}
