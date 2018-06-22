package com.mail.util;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import net.sf.json.JSONArray;

/**
 * <p>
 * 
 * <p/>
 * @author 作者 WJM:
 * @version 创建时间：2018年3月26日
 */
public class MailUtils {

	/*public static void main(String[] args) {
		sendTextMail("qjyn1314foxmail.com","mail工具类测试","发送成功！");
	}*/
	
	private static Logger logger = LoggerFactory.getLogger(MailUtils.class);

	private String mailFromDefault;
	public void setMailFromDefault(String mailFromDefault) {
		this.mailFromDefault = mailFromDefault;
	}

	private String mailFromPersonalDefault;
	public void setMailFromPersonalDefault(String mailFromPersonalDefault) {
		this.mailFromPersonalDefault = mailFromPersonalDefault;
	}
	
	private JavaMailSender mailSender;
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	/**
	 * 发送普通文本邮件
	 * @param toEmail 	邮件接收者地址
	 * @param subject 	邮件主题
	 * @param content	邮件文本内容
	 * @return	false：邮件发送失败，true：邮件已发送（只表示邮件已发送出去，不表示邮件一定发送成功）
	 */
	public boolean sendTextMail(String toEmail, String subject, String content) {
    	if(StringUtils.isBlank(toEmail)){
    		logger.error("邮件接收者地址不能为空");
    		return false;
    	}
    	
		String[] toEmails = {toEmail};
		return sendTextMail(toEmails, subject, content);
	}
	
	/**
	 * 发送普通文本邮件
	 * 
	 * @param toEmails 	邮件接收者地址
	 * @param subject 	邮件主题
	 * @param content	邮件文本内容
	 * 
	 * @return	false：邮件发送失败，true：邮件已发送（只表示邮件已发送出去，不表示邮件一定发送成功）
	 */
	private boolean sendTextMail(String[] toEmails, String subject, String content) {
		try {
	    	if(toEmails == null || toEmails.length == 0){
	    		logger.error("邮件接收者地址不能为空");
	    		return false;
	    	}
	    	
	    	if(StringUtils.isBlank(subject)){
	    		logger.error("邮件主题不能为空");
	    		return false;
	    	}

	    	if(StringUtils.isBlank(content)){
	    		logger.error("邮件文本内容不能为空");
	    		return false;
	    	}
	    	
			logger.info("准备发送普通文本邮件 . . .");
			logger.info("邮件接收者："+JSONArray.fromObject(toEmails)+"，邮件主题："+subject+"，邮件文本内容："+content);
			
			MimeMessage mMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mMessage, false, "UTF-8");
			helper.setFrom(mailFromDefault, mailFromPersonalDefault);
			helper.setTo(toEmails);
			helper.setSubject(subject);
			helper.setText(content);
			
			mailSender.send(mMessage);
			
			logger.info("普通文本邮件已发送.");
			
			return true;
		} catch (Exception e) {
			logger.error("普通文本邮件发送异常！", e);
			return false;
		}
	}

}
