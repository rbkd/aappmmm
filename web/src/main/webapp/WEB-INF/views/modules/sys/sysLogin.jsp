<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')} 登录</title>
	<meta name="decorator" content="default"/>
	<style type="text/css">
      html,body,table{background-color:#f5f5f5;width:100%;height:500px;text-align:center;}.form-signin-heading{font-size:36px;margin-bottom:20px;color:#0663a2;}
      .form-signin{position:relative;text-align:left;width:300px;padding:25px 29px 29px;margin:0 auto 20px;background-color:#fff;border:1px solid #e5e5e5;
        	-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;-webkit-box-shadow:0 1px 2px rgba(0,0,0,.05);-moz-box-shadow:0 1px 2px rgba(0,0,0,.05);box-shadow:0 1px 2px rgba(0,0,0,.05);}
      .form-signin .checkbox{margin-bottom:10px;color:#0663a2;} .form-signin .input-label{font-size:16px;line-height:23px;color:#999;}
      .form-signin .input-block-level{font-size:16px;height:auto;margin-bottom:15px;padding:7px;*width:283px;*padding-bottom:0;_padding:7px 7px 9px 7px;}
      .form-signin #themeSwitch{position:absolute;right:15px;bottom:10px;}
      .form-signin div.validateCode {padding-bottom:15px;} .mid{vertical-align:middle;}
      .header{height:60px;padding-top:30px;} .alert{position:relative;width:300px;margin:0 auto;*padding-bottom:0px;}
      label.error{background:none;padding:2px;font-weight:normal;color:inherit;margin:0;}
    </style>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$("#inputFormpw").validate({
				rules: {
				},
				messages: {
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio") || element.parent().hasClass("input-append")){
						error.appendTo(element.parents(".controls:first"));
					} else {
						error.insertAfter(element);
					}
				}
			});		
			
			$("#loginForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					username: {required: "请填写用户名."},password: {required: "请填写密码."},
					validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
		});
		
		 function log(){
			 
				$("#username").val("tourist");
				$("#password").val("tourist1");
				//loading('正在登录，请稍等...');
				$("#loginForm").submit();
		    }	
		
		// 如果在框架中，则跳转刷新上级页面
		if(self.frameElement && self.frameElement.tagName=="IFRAME"){
			parent.location.reload();
		}
	</script>
</head>
<body>
	<div class="header"><%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
		<c:choose>
			<c:when test="${ernum<4}">
				<div id="messageBox" class="alert alert-error <%=error == null ? "hide" : ""%>">
					<button data-dismiss="alert" class="close">×</button>
					<span id="loginError" class="error"><%=error == null ? "" : "resite.modules.sys.security.CaptchaException".equals(error) ? "验证码错误, 请重试.": "用户或密码错误, 请重试."%></span>
				</div>
			</c:when>
			<c:when test="${empty ernum}">
				<div id="messageBox" class="alert alert-error <%=error == null ? "hide" : ""%>">
					<button data-dismiss="alert" class="close">×</button>
					<span id="loginError" class="error"><%=error == null ? "" : "resite.modules.sys.security.CaptchaException".equals(error) ? "验证码错误, 请重试.": "用户或密码错误, 请重试."%></span>
				</div>
			</c:when>			
			<c:otherwise>
				<div id="messageBox" class="alert alert-error ">
					<button data-dismiss="alert" class="close">×</button>
					<span id="loginError" class="error">账号输入错误5次，冻结账号，请联系管理员</span>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<c:choose>
		<c:when test="${not empty pwd}">
			<form:form id="inputFormpw" modelAttribute="user" action="${ctx}/login" method="post" class="form-signin">
				<input id="uid" name="uid" type="hidden" value="${user.id}"/>
				<input id="username" name="username" type="hidden" value=""/>
				<tags:message content="${message}"/>
				<div class="control-group">
					<span class="control-label">密码:</span>
					<div class="controls">
						<input id="newPassword" name="newPassword" type="password"
							value="" maxlength="100" minlength="1"
							class="required" />
					</div>
				</div>
				
				<div class="control-group">
					<span class="control-label">确认密码:</span>
					<div class="controls">
						<input id="confirmNewPassword" name="confirmNewPassword"
							type="password" value="" maxlength="100" minlength="1"
							class="required" equalTo="#newPassword" />
					</div>
				</div>

				<div class="form-actions">
					<input id="btnSubmit1" class="btn btn-primary" type="submit" value="保 存" />
				</div>
			</form:form>
		</c:when>
		<c:otherwise>			
	<h1 class="form-signin-heading">${fns:getConfig('productName')}</h1>
	<form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
	<input id="error" name="error" type="hidden" value="<%=error%>"/>
	<input id="ernum" name="ernum" type="hidden" value="${ernum}"/>
	<input id="oldename" name="oldname" type="hidden" value="${oldname}"/>
		<span class="input-label" for="username">登录名</span>
		<input type="text" id="username" name="username" class="input-block-level required" value="${username}">
		<span class="input-label" for="password">密码</span>
		<input type="password" id="password" name="password" class="input-block-level required">
	 	<c:if test="${isValidateCodeLogin}">  <div class="validateCode">
			<span class="input-label mid" for="validateCode">验证码</span>
			<tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
		</div> </c:if>  
		<input class="btn btn-primary" type="submit" value="登 录"/>&nbsp;&nbsp;
		<span for="rememberMe" title="下次不需要再登录"><input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我（公共场所慎用）</span>
		<div id="themeSwitch" class="dropdown">
			<a class="dropdown-toggle" href="#" id="but" onclick="log();">游客登录</a>
			<!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
		</div>
	</form>
	Copyright &copy; 2012-${fns:getConfig('copyrightYear')} <a href="${pageContext.request.contextPath}${fns:getFrontPath()}">${fns:getConfig('productName')}</a> - Powered By veasy ${fns:getConfig('version')}
	</c:otherwise>
	</c:choose>
	
	
	<script type="text/javascript" src="${ctxStatic}/frontEnd/js/jquery.cookie.js"></script>
    <script type="text/javascript">
      $(function(){
    	  var cookie_name=$.cookie("name");
    	  var cookie_password=$.cookie("password");
    	  
    	  if(cookie_name&&cookie_password){
    		  $("#username").val(cookie_name);
    		  $("#password").val(cookie_password);
    		  $("#rememberMe").attr("checked",true);
    	  }
    	  
    	  $("#rememberMe").click(function(){
    		  if($(this).attr("checked")){
    			  var name= $("#username").val();
        		  var password= $("#password").val();
        		  if(name!=""&&password!=""){
        			  $.cookie("name",name,{ path: '/', expires: 10 });
        			  $.cookie("password",password,{ path: '/', expires: 10 });
        		  } 
    		  }else{
    			  $.cookie("name",null,{ path: '/', expires: 10 });
    			  $.cookie("password",null,{ path: '/', expires: 10 });
    		  }  		    		  
    	  });
      })
    </script>
</body>
</html>