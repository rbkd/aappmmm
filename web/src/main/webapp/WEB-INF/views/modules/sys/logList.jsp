<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日志管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			loading('正在提交，请稍等...');
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/sys/log/">日志ghhgh列表</a></li>
	</ul>
	<form:form id="searchForm" action="${ctx}/sys/log/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div>
			<label>用户ID：</label><input id="createById" name="createById" type="text" maxlength="50" class="input-medium" value="${createById}"/>
			<label>URI：</label><input id="uri" name="uri" type="text" maxlength="50" class="input-medium" value="${uri}"/>
			<label>日期范围：&nbsp;</label><input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="${beginDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="${endDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			&nbsp;<label for="exception"><input id="exception" name="exception" type="checkbox"${exception eq '1'?' checked':''} value="1"/>只查询异常信息</label>&nbsp;&nbsp;
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th></th><th>所在公司</th><th>所在部门</th><th>操作用户</th><th>URI</th><th>提交方式</th><th>操作者IP</th><th>创建时间</th></thead>
		<tbody>
		<c:forEach items="${page.list}" var="log">
			<tr>
				<td><a href="javascript:" onclick="$('#c_${log.id}').toggle()">查看详情</a></td>
				<td>${log.createBy.company.name}</td>
				<td>${log.createBy.office.name}</td>
				<td>${log.createBy.name}</td>
				<td><strong>${fns:abbr(log.uri,50)}</strong></td>
				<td>${log.method}</td>
				<td>${log.accessorIp}</td>
				<!-- <td><fmt:formatDate value="${log.createDate}" type="both"/></td> -->
				<td><tags:prettyTime date="${log.createDate}"></tags:prettyTime></td>
			</tr>
			<tr id="c_${log.id}" style="background:#fdfdfd;display:none;">
				<td colspan="9">提交参数: ${fns:escapeHtml(log.params)}
				<c:if test="${not empty log.exception}"><br/>异常信息: <br/><%request.setAttribute("strEnter", "\n"); %>
				${fn:replace(fns:escapeHtml(log.exception), strEnter, '<br/>')}</c:if></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>