package com.shoory.framework.starter.sms.lingkai;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.shoory.framework.starter.sms.SmsSender;

@Component
public class LingKaiSmsSender implements SmsSender {
	@Value("${lingkai.sms.corpid}")
	private String corpID;
	@Value("${lingkai.sms.pwd}")
	private String pwd;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final String URL = "https://mb345.com/wsn/BatchSend2.aspx?CorpID={1}&Pwd={2}&Mobile={3}&Content={4}&SendTime=&cell=";
	private Logger logger = LoggerFactory.getLogger(LingKaiSmsSender.class);
	
	@Override
	public boolean sendSms(String nationCode, String phoneNumber, String templateId, String[] params, String smsSign) {
		ResponseEntity<String> result;
		try {
			result = restTemplate.getForEntity(URL, 
					String.class, 
					this.corpID, 
					this.pwd, 
					phoneNumber, 
					new String(templateId.getBytes("utf-8"), "gb2312"));

			logger.info(result.getBody());
			//

			if (!result.hasBody()) {
				throw new RuntimeException("连接通信网关失败");
			} else {
				//提交成功
				int returnValue = Integer.valueOf(result.getBody());
				if (returnValue < 0) {
					/**
					 *  -1	账号未注册
		-2	其他错误
		-3	帐号或密码错误
		-5	余额不足，请充值
		-6	定时发送时间不是有效的时间格式
		-7	提交信息末尾未签名，请添加中文的企业签名【 】
		-8	发送内容需在1到300字之间
		-9	发送号码为空
		-10	定时时间不能小于系统当前时间
		-100	IP黑名单
		-102	账号黑名单
		-103	IP未导白
					 */
					
					throw new RuntimeException("发送验证码出错：代码" + result.getBody());
				}
			}
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

}
