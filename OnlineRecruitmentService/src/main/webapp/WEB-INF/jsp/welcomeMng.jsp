<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.UserBean"%>
<%@ page import="com.ors.bean.JobBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome Manager</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<center>
		<h2>--- Job List ---</h2>
	</center>
	<c:if test="${jobs != null }">
		<table cellspacing="25" border="0" align="center">
			<tr align="center">
				<td>ID</td>
				<td>Closing Date</td>
				<td>Status</td>
				<td>Operations</td>
			</tr>
			<c:forEach var="job" items="${jobs}">
				<tr align="center">
					<td><b><a href="detail?jobId=${job._jobId}"
							title="quickview">${job._jobId}</a></b></td>
					<td><c:out value="${job.closingDate}" /></td>
					<td><c:out value="${job.status}" /></td>
					<td><c:if test="${job.status eq 'created'}">
				*<a href="jobupdating?jobId=${job._jobId}" title="update">Update</a>*
			</c:if> <c:if test="${(job.status eq 'created') or (job.status eq 'fixed')}">
				*<a href="jobclose?jobId=${job._jobId}" title="close">Close</a>*
			</c:if>
			<c:if test="${(job.status eq 'finalized') or (job.status eq 'created')}">
				*<a href="jobdeletion?jobId=${job._jobId}" title="delete">Archive</a>*
			</c:if></td>
				</tr>
			</c:forEach>
		</table>
		<p>
	</c:if>
	<c:if test="${jobs == null }">No job postings</c:if>
	<p>
</body>
</html>