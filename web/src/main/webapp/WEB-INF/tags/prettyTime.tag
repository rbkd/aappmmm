<%@ tag import="apm.common.utils.PrettyTimeUtils" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="date" type="java.util.Date" required="true" description="时间" %>
<span title="${date}"><%=PrettyTimeUtils.prettyTime(date)%></span>