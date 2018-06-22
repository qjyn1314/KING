package com.sms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.sms.entity.request.SmsSendRequest;
import com.sms.entity.request.SmsVariableRequest;
import com.sms.entity.response.SmsSendResponse;
import com.sms.entity.response.SmsVariableResponse;




/**
 * 
* @Description: 短信工具类
* @author huixiaoyong 
* @date 2018年1月11日 下午6:12:19 
*
 */
public class SmsUtil {
	/*
sms.send.url=http://smssh1.253.com/msg/send/json
sms.send.variable.url=http://smssh1.253.com/msg/variable/json
sms.balance.url=http://smssh1.253.com/msg/balance/json
sms.account=N2216074
sms.password=lsXrLnDgE2aa66
	 */
	// 用户平台API账号(非登录账号,示例:N1234567)
	private static String account = "";
	// 用户平台API密码(非登录密码)
	private static String pswd = "";
	//请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
	private static String requestServerUrl = "";
	private static String requestServerUrlVariable = "";
	private static Logger logger = LoggerFactory.getLogger(SmsUtil.class);
	static {
		account = getProperty("sms.account");
		pswd = getProperty("sms.password");
		requestServerUrl = getProperty("sms.send.url");
		requestServerUrlVariable = getProperty("sms.send.variable.url");
	}
	
   /**
	* @Description: 以POST方式发送HTTP 请求 
	* @param  path
	* @param  postContent
	* @return String    返回类型 
	* @throws
    */
	private static String post(String path, String postContent) {
		URL url = null;
		try {
			if(StringUtils.isNotBlank(path)) {
				url = new URL(path);
			}else {
				return null;
			}
			
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("POST");// 提交模式
			httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
			httpURLConnection.setReadTimeout(10000);//读取超时 单位毫秒
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			httpURLConnection.connect();
			OutputStream os=httpURLConnection.getOutputStream();
			os.write(postContent.getBytes("UTF-8"));
			os.flush();
			
			StringBuilder sb = new StringBuilder();
			int httpRspCode = httpURLConnection.getResponseCode();
			if (httpRspCode == HttpURLConnection.HTTP_OK) {
				// 开始获取数据
				BufferedReader br = new BufferedReader(
						new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();

			}

		} catch (Exception e) {
			logger.error("request post error.", e);
		}
		return null;
	}
	
	private static String getProperty(String keyName) {
		if(StringUtils.isEmpty(keyName))
			return null;
		Properties props = new Properties();
		InputStream in = null;
		
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/properties/sms.properties");
			if(in!=null) {
				props.load(in);
			}else {
				logger.error("properties file is not found.");
			}
		}  catch (IOException e) {
			logger.error("properties file reading error.", e);
		}catch (Exception e1) {
			logger.error("properties file is not found.", e1);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("关闭输入流报错", e);
				}
			}
		}

		Iterator<String> it = props.stringPropertyNames().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if(key.equals(keyName)){
				return props.getProperty(key);
			}
		}
		return null;
	}
	
	public static boolean send(String msg,String phone) {
		boolean re=false;		
		//状态报告
		String report= "false";
		StringBuilder errSB=new StringBuilder();
		
		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,report);
		String requestJson = JSON.toJSONString(smsSingleRequest);
		String response = SmsUtil.post(requestServerUrl, requestJson);
		
		logger.info("request："+requestJson);
		
		if(StringUtils.isNotBlank(response)) {
			try {
				SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
				
				if(smsSingleResponse!=null) {
					if(StringUtils.isNotBlank(smsSingleResponse.getCode()) && smsSingleResponse.getCode().equals("0")) {
						re=true;
					}else {
						errSB.append(StringUtils.isNotBlank(smsSingleResponse.getErrorMsg())?smsSingleResponse.getErrorMsg():"");
					}
				}
				
			}catch(Exception e) {
				errSB.append(e.getMessage());
			}
			
			logger.info("response："+response);
		}
		
		if(re) {
			logger.info("短信发送成功！");
		}else {
			logger.error("短信发送失败！"+errSB.toString());
		}
		
		return re;
	}
	
	public static boolean sendVariable(String msg,String params) {
		boolean re=false;		
		//状态报告
		String report= "false";
		StringBuilder errSB=new StringBuilder();
		
		SmsVariableRequest smsVariableRequest = new SmsVariableRequest(account, pswd, msg, params,report);
		String requestJson = JSON.toJSONString(smsVariableRequest);
		String response = SmsUtil.post(requestServerUrlVariable, requestJson);
		
		logger.info("request："+requestJson);
		
		if(StringUtils.isNotBlank(response)) {
			try {
				SmsVariableResponse smsVariableResponse = JSON.parseObject(response, SmsVariableResponse.class);
				
				if(smsVariableResponse!=null) {
					if(StringUtils.isNotBlank(smsVariableResponse.getCode()) && smsVariableResponse.getCode().equals("0")) {
						re=true;
					}else {
						errSB.append(StringUtils.isNotBlank(smsVariableResponse.getErrorMsg())?smsVariableResponse.getErrorMsg():"");
					}
				}
				
			}catch(Exception e) {
				errSB.append(e.getMessage());
			}
			
			logger.info("response："+response);
		}
		
		if(re) {
			logger.info("sms send success！");
		}else {
			logger.error("sms send fail！"+errSB.toString());
		}
		
		return re;
	}
	
	public static void main(String[] args) {
		System.out.println("开始");
		boolean re=SmsUtil.send("【智链金服】测试短信内容，请查收。 ","15321355715");
		System.out.println("结束");
		//boolean re=SmsUtil.sendVariable("【智链金服】测试变量短信内容，您的验证码是：{$var}。","13520491426,2351");
		System.out.println(re);
		System.out.println("输出");
	}
	

}
