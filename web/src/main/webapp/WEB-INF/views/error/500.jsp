<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="apm.common.beanvalidator.BeanValidators"%>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%response.setStatus(200);%>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(ex.getMessage(), ex);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>500 - 系统内部错误</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
	<div class="container-fluid">
		<div id="page-content" class="clearfix">
			<div class="row-fluid">
				<!-- PAGE CONTENT BEGINS HERE -->
				<div class="error-container">
					<div class="well">
						<h1 style="color:red;" class="lighter">系统发生内部错误。</h1>
						<hr />
						
						<div>
							<p>错误信息：</p><p>
								<%
									if (ex!=null){
										if (ex instanceof javax.validation.ConstraintViolationException){
											for (String s : BeanValidators.extractPropertyAndMessageAsList((javax.validation.ConstraintViolationException)ex, ": ")){
												out.print(s+"<br/>");
											}
										}else{
											out.print(ex+"<br/>");
										}
									}
								%>
							</p>
						</div>
						
						<hr />
						<div class="space"></div>
						<div class="row-fluid">
							<div class="center">
								<a href="javascript:" onclick="history.go(-1);" class="btn btn-grey"><i class="icon-arrow-left"></i>
									&nbsp;返回上一页</a> <a href="${ctx}#" class="btn btn-primary"><i
									class="icon-home"></i>&nbsp;返回首页</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script>try{top.$.jBox.closeTip();}catch(e){}</script>		
	</div>	
</body>
</html>