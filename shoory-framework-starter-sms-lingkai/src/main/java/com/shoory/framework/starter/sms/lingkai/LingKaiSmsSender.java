package com.shoory.framework.starter.sms.lingkai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.shoory.framework.starter.sms.SmsSender;

@Component
public class LingKaiSmsSender implements SmsSender {
	@Value("${lingkai.sms.corpid}")
	private String corpID;
	@Value("${lingkai.sms.pwd}")
	private String pwd;
	
	private final String URL = "https://服务器地址/wsn/BatchSend2.aspx?CorpID={1}&Pwd={2}&Mobile={3}&Content={4}&SendTime=&cell=";

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public boolean sendSms(String nationCode, String phoneNumber, String templateId, String[] params, String smsSign) {
		ResponseEntity<Integer> result =  restTemplate.getForEntity(URL, 
				Integer.class, this.corpID, this.pwd, phoneNumber, templateId);
		// 
		if (result.hasBody() && result.getBody() > 0) {	//提交成功
			return true;
		} else {
			if (result.hasBody()) {
				throw new RuntimeException("发送验证码出错：代码" + result.getBody());
			} else {
				throw new RuntimeException("连接通信网关失败");
			}
			
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
		}
	}

}
