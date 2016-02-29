<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.ApplicationBean"%>
<%@ page import="com.ors.bean.UserBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome Reviewer</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<center>
		<h2>--- Assigned Application List ---</h2>
	</center>
	<c:if test="${not (apps eq null) }">
		<table cellspacing="25" border="0" align="center">
			<tr align="center">
				<td>ID</td>
				<td>Status</td>
				<td>&nbsp;</td>
			</tr>

			<c:forEach var="app" items="${apps}">
				<tr align="center">
					<td><c:out value="${app._appId}" /></td>
					<td><b><c:out value="${app.status}" /></b></td>
					<td><a href="appdetail?appId=${app._appId}" title="quickview">Process</a></td>
				</tr>
			</c:forEach>
		</table>
	</c:if>

	<c:if test="${apps eq null}">
		<center><b>No application is assigned to you now</b></center>
	</c:if>
</body>
</html>