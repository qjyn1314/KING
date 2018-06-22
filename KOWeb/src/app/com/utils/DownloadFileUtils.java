package app.com.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class DownloadFileUtils {

	/**
	 * 下载文件
	 * 
	 * @param filePath	文件路径（相对于应用根目录路径）
	 * @param downloadFileName	下载文件名
	 * @throws IOException 
	 */
	public static void downloadFile(String filePath, String downloadFileName, HttpServletRequest request, HttpServletResponse response) throws Exception{
		try {
			String modelFilePath = request.getSession().getServletContext().getRealPath(filePath);
			downloadFileAbsolutePath(modelFilePath, downloadFileName, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 下载文件
	 * 
	 * @param modelFilePath	文件路径（绝对路径）
	 * @param downloadFileName	下载文件名
	 * @throws IOException 
	 */
	private static void downloadFileAbsolutePath(String modelFilePath, String downloadFileName, HttpServletRequest request, HttpServletResponse response) throws Exception{
		try {
			InputStream inputStream = new FileInputStream(modelFilePath);
			byte[] b = new byte[inputStream.available()];
			inputStream.read(b, 0, inputStream.available());
			inputStream.close();
	        
	        createDefaultDownload(b, downloadFileName, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 生成文件
	 * 
	 * @param bytes
	 * @param fileName
	 * @param request
	 * @param response
	 */
	private static void createDefaultDownload(byte[] bytes, String fileName
			, HttpServletRequest request, HttpServletResponse response){
		String contentType = downloadFileContentType(fileName);
		createDownload(bytes, fileName, contentType, request, response);
	}
	
	
	/**
	 * 获取下载文件的类型
	 * 
	 * @param fileNameWithExt
	 * @return
	 */
	private static String downloadFileContentType(String fileNameWithExt){
		String contentType = "multipart/form-data";
		
		if(fileNameWithExt != null){
			fileNameWithExt = StringUtils.trimToEmpty(fileNameWithExt);
			String fileExtName = fileNameWithExt.substring(fileNameWithExt.lastIndexOf("."));
			if(".xls".equalsIgnoreCase(fileExtName) || ".xlsx".equalsIgnoreCase(fileExtName)){
				contentType = "application/vnd.ms-excel";
			}
			else if(".doc".equalsIgnoreCase(fileExtName) || ".docx".equalsIgnoreCase(fileExtName)){
				contentType = "application/msword";
			}
			else if(".ppt".equalsIgnoreCase(fileExtName) || ".pptx".equalsIgnoreCase(fileExtName)){
				contentType = "application/vnd.ms-powerpoint";
			}
			else if(".pdf".equalsIgnoreCase(fileExtName)){
				contentType = "application/pdf";
			}
			else if(".jpg".equalsIgnoreCase(fileExtName) || ".jpeg".equalsIgnoreCase(fileExtName)){
				contentType = "image/jpeg";
			}
			else if(".gif".equalsIgnoreCase(fileExtName)){
				contentType = "image/gif";
			}
			else if(".txt".equalsIgnoreCase(fileExtName)){
				contentType = "text/plain";
			}
			else if(".html".equalsIgnoreCase(fileExtName)){
				contentType = "text/html";
			}
		}
		
		return contentType;
	}
	
	/**
	 * 生成报表下载文件
	 * 
	 * @param bytes
	 * @param fileName
	 * @param contentType
	 * @param request
	 * @param response
	 */
	private static void createDownload(byte[] bytes, String fileName, String contentType
			, HttpServletRequest request, HttpServletResponse response) {
		try {
			String attachmentName = fileName;
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
				attachmentName = URLEncoder.encode(attachmentName, "UTF-8");
			} else {
				attachmentName = new String(attachmentName.getBytes("UTF-8"), "ISO8859-1");
			}
			
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			ba.write(bytes);
			
			response.setHeader("Content-disposition", "attachment; filename=" + attachmentName);  //弹出保存对话框的
			//response.setHeader("Content-disposition", "inline; filename=" + attachmentName);//直接打开
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setDateHeader("Expires", (System.currentTimeMillis() + 1000));
			response.setContentLength(ba.size());
			response.setContentType(contentType);

			ServletOutputStream out = response.getOutputStream();
			ba.writeTo(out);
			
			out.flush();
			
			out.close();
			ba.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
