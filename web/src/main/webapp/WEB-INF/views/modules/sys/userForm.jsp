<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/uploadify/uploadify.css"></link>	
	<style>
		.uploadify-button {
			background-color: rgb(67, 145, 187);
			background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0, rgb(67,
				145, 187) ), color-stop(1, rgb(67, 145, 187) ) );
			max-width: 70px;
			max-height: 20px;
			border-radius: 1px;
			border: 0px;
			font: bold 12px Arial, Helvetica, sans-serif;
			display: block;
			text-align: center;
			text-shadow: 0 0px 0 rgba(0, 0, 0, 0.25);
		}
		
		.uploadify:hover .uploadify-button {
			background-color: rgb(67, 145, 187);
			background-image: -webkit-gradient(linear, left bottom, left top, color-stop(0, rgb(67,
				145, 187) ), color-stop(1, rgb(67, 145, 187) ) );
		}
	</style>
	<script type="text/javascript" src="${ctxStatic}/uploadify/jquery.uploadify-3.1.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/uploadify/jquery.jalert.js"></script>
	
	<script type="text/javascript">
		jQuery(function() {
			jQuery("#upIcon").uploadify(
					{
						'height' : 20,
						'width' : 70,
						'buttonText' : '修改',
						'multi' : false,
						'simUploadLimit' : 1,
			            'swf' : '${ctxStatic}/uploadify/uploadify.swf',
			            'uploader' : '${ctx}/sys/user/o_upload;jsessionid=${pageContext.session.id}',
						'auto' : true,
						'fileTypeExts' : '*.jpg;*.png;*.jpeg;*.bmp;',
						'onUploadStart' : function(file) {
							jQuery("#upIcon").uploadify("settings", "formData", {
								'folder' : 'Folder'
							});
						},
						'onUploadSuccess' : function(file, data, Response) {
							if (Response) {
								var objvalue = eval("(" + data + ")");
								if (jQuery("#icoUrl")) {
									jQuery("#icoUrl").val(file.name);
								}
								
								if (jQuery("#imgshow")) {
									jQuery("#imgshow").attr('src', '${actx}/' + objvalue.filePath);
								}
								jQuery("#icoUrl").val(objvalue.filePath);
								
								jQuery("#imgend").attr('src', '${actx}/' + objvalue.filePath);
							}
						}
					});
		});
		
		// 
		$(document).ready(function() {
			$("#loginName").focus();
			$("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/form?id=${user.id}">用户<shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<c:if test="${not empty user.id}">
			<form:hidden path="id"/>
		</c:if>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">归属公司:</label>
			<div class="controls">
                <tags:treeselect id="company" name="company.id" value="${user.company.id}" labelName="company.name" labelValue="${user.company.name}"
					title="公司" url="/sys/office/treeData?type=1" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属部门:</label>
			<div class="controls">
                <tags:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">头像:</label>
			<div class="controls">
				<input type="hidden" id="icoUrl" name="icoUrl" value="${user.icoUrl}" />
				<table cellpadding="0" cellspacing="0">
					<tr >
						<td>
							<a>
								<c:choose>
									<c:when test="${not empty user.icoUrl}">
										<img id="imgshow" style="width: 70px; height: 70px" align="bottom" src="${actx}/${user.icoUrl}" />
									</c:when>
									<c:otherwise>
										<img id="imgshow" style="width: 70px; height: 70px" src="${ctxStatic}/images/face-placeholder.png">
									</c:otherwise>
								</c:choose>
							</a>
							<span class="help-inline">支持JPG\JPEG、PNG、BMP格式的图片<br /> 建议文件小于2M。</span>
						</td>
					</tr>
					<tr>
						<td>
							<div style="position: relative;top:-20px;">
								<input type="file" id="upIcon" name="upIcon" />
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">登录名:</label>
			<div class="controls">
				<input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
				<form:input path="loginName" htmlEscape="false" maxlength="100" class="required userName"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">工号:</label>
			<div class="controls">
				<form:input path="no" htmlEscape="false" maxlength="100" minlength="1"  class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100"  minlength="1"  class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">密码:</label>
			<div class="controls">
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="100" minlength="1" class="${empty user.id?'required':''}"/>
				<c:if test="${not empty user.id}"><span class="help-inline">若不修改密码，请留空。</span></c:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">确认密码:</label>
			<div class="controls">
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="100" minlength="1" equalTo="#newPassword"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="200" class="email"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机:</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="255" class="input-xlarge"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户类型:</label>
			<div class="controls">
				<form:select path="userType">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false" maxlength="100"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户角色:</label>
			<div class="controls">
				<form:checkboxes path="roleIdList" items="${allRoles}" itemLabel="name" itemValue="id" htmlEscape="false" class="required"/>
			</div>
		</div>
		<c:if test="${not empty user.id}">
			<div class="control-group">
				<label class="control-label">创建时间:</label>
				<div class="controls">
					<label class="lbl"><tags:prettyTime date="${user.createDate}"></tags:prettyTime><!-- <fmt:formatDate value="${user.createDate}" type="both" dateStyle="full"/> --></label>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">最后登陆:</label>
				<div class="controls">
					<label class="lbl">IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<tags:prettyTime date="${user.loginDate}"></tags:prettyTime><!-- <fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/> --></label>
				</div>
			</div>
		</c:if>
		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>