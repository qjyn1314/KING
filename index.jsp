<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">

</script>
</head>
<body>
<p>短信</p>
<form action="bean/hello.do" method="post" >
手机号：<input type="text" name="phone" ><br/>
发送的信息：<input type="text" name="code">
<input type="submit" value="发送"/>
</form>
         
       <button type="button" onclick="window.open('bean/download.do')"> 下载模板 </button>
        <a href="bean/download.do">下载模板</a>
        
   <!-- 	<form action="bean/download" method="post">
			<input type="submit" value="form下载模板">
	</form>   -->
	
<!-- 	<form action="bean/download" enctype="multipart/form-data" method="post">  
    <input type="submit" value="下载Excel"></input>     
	</form>   -->
	
	
</body>
</html>