package app.com.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sms.util.SmsUtil;

import app.com.utils.DownloadFileUtils;

/**
 * <p>
 * 
 * <p/>
 * @author 浣滆�� WJM:
 * @version 鍒涘缓鏃堕棿锛�2018骞�3鏈�21鏃�
 */
@Controller
@RequestMapping("/bean")
public class BeanController {

	@RequestMapping(value="/hello",method=RequestMethod.POST)
	public String HelloWord(String phone,String code,Model model) {
        //15321355715
		//銆愭櫤閾鹃噾鏈嶃�戞祴璇曠煭淇�
		Logger logger = LoggerFactory.getLogger(BeanController.class);
		logger.info("phone____"+phone+"code____"+code);
		//boolean s = MailUtils.sendTextMail("qjyn1314foxmail.com","mail宸ュ叿绫绘祴璇�","鍙戦�佹垚鍔燂紒");
		boolean b = SmsUtil.send(code, phone);
		if(b) {
			model.addAttribute("phone", phone);
			model.addAttribute("code", code);
		}else {
			logger.error("______鍙戦�佺煭淇″け璐�");
		}
		return "hello";
	}
	
	    @RequestMapping("/download")
	    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
	       System.out.println("进入下载xlsx方法"); 
	       DownloadFileUtils.downloadFile("WEB-INF/excel/paymentAccountTemplate.xlsx", "模板文件.xlsx", request, response);
		   System.out.println("下载xlsx方法调用结束"); 
	    }
	    
	    
}
