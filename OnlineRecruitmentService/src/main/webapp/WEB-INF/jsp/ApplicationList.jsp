<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.ApplicationBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Application List</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<center>
		<h2>--- Application List ---</h2>
	</center>
	<c:if test="${not (apps eq null) }">
		<table border="0" cellspacing="25" align="center">
			<tr align="center">
				<td>ID</td>
				<td>Status</td>
				<td>Assign</td>
				<td>Reviews</td>
				<td>Invite</td>
				<td>Archive</td>
				<td>Details</td>
			</tr>
			<c:forEach var="app" items="${apps}">
				<tr align="center">
					<td align="center"><c:out value="${app._appId}" /></td>
					<td><b><c:out value="${app.status}" /></b></td>
					<td><c:if
							test="${(app.status eq 'created') or (app.status eq 'autochecked')}">
							<a href="assignment?appId=${app._appId}">Assign</a>
						</c:if></td>
					<td><c:if
							test="${not (app.status eq 'created') and not (app.status eq 'autochecked')}">
							<a href="reviewdetail?appId=${app._appId}">Reviews</a>
						</c:if></td>
					<td><c:if test="${app.status eq 'shortlisted'}">
							<a href="invite?appId=${app._appId}">Send Invitation</a>
						</c:if></td>
					<td><c:if
							test="${(app.status eq 'notshortlisted') or (app.status eq 'itv_rejected') or (app.status eq 'rjct_notified') or (app.status eq 'acpt_notified')}">
							<a href="archive?appId=${app._appId}">Archive</a>
						</c:if></td>
					<td><a href="appdetail?appId=${app._appId}" title="quickview">See
							More</a></td>
			</c:forEach>

		</table>
	</c:if>
	<c:if test="${apps == null}">no applications now</c:if>
</body>
</html>