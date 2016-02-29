<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.ReviewBean"%>
<%@ page import="com.ors.bean.ApplicationBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Review Details fro Application ${app._appId}</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<c:if test="${revs eq null}">
		This Application hasn't been reviewed yet. Please wait.
	</c:if>
	<c:if test="${not (revs eq null)}">
	*Application:&nbsp;&nbsp;${app._appId}*&nbsp;&nbsp;&nbsp;&nbsp;Status:&nbsp;&nbsp;<font color="red"><b>${app.status}</b></font>
	<p>
			<c:forEach var="rev" items="${revs}">
	Reviewer: ${rev._uId} <p>Comments: ${rev.comments}
				<p>Decision: ${rev.decision}
				<p>--------------------------------------
				<p>
			</c:forEach>
			<c:if test="${(count_revs == 2) and (app.status eq 'in_review')}">
				<a href="shortlist?appId=${app._appId}"> Make Final Short List</a>
			</c:if>
			<c:if test="${(is_finished == 2) and (app.status eq 'itv_accepted')}">
				<a href="finalresult?appId=${app._appId}"> Make Final Result</a>
			</c:if>
	</c:if>
</body>
</html>