<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.UserBean"%>
<%@ page import="com.ors.bean.JobBean"%>
<%@ page import="com.ors.bean.ApplicationBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Job Quick View</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<center>
		<div>
			<h2>--- Job Information ---</h2>
			Job:
			<c:out value="${job._jobId}" />
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			Department:
			<c:out value="${job.department}" />
			<p>
				<c:out value="Closing Date: ${job.closingDate}" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:out value="Location: ${job.location}" />
			<p>
				<c:out value="Position Type: ${job.positionType}" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:out value="Salary: ${job.salary}" />
			<p>
				<c:out value="Descriptions: ${job.jobDescriptions}" />
		</div>

		<c:if
			test="${(user eq null) and ((job.status eq 'created') or (job.status eq 'fixed'))}">
			<a href="application?jobId=${job._jobId}">Apply</a>
		</c:if>
		<c:if test="${user.role eq 'manager'}">

			<c:if test="${(job.status eq 'created')}">
					*<a href="jobupdating?jobId=${job._jobId}" title="Update">Update</a>*
				</c:if>

			<c:if test="${(job.status eq 'created') or (job.status eq 'fixed')}">
				*<a href="jobclose?jobId=${job._jobId}" title="Close">Close</a>*
			</c:if>
			<c:if test="${(job.status eq 'closed') and (allDone eq 'yes')}">
				*<a href="notification?jobId=${job._jobId}" title="Notify">Send Notifications</a>*
			</c:if>
			<c:if
				test="${(job.status eq 'finalized') or (job.status eq 'created') or ((apps eq null) and (job.status eq 'closed'))}">
				*<a href="jobdeletion?jobId=${job._jobId}" title="Delete">Archive</a>*
			</c:if>
		</c:if>
		<p>
	</center>
	<center>
		<c:if test="${user.role eq 'manager'}">
			<h2>--- Corresponding Applications ---</h2>
			<c:if test="${not (apps eq null) }">
				<c:forEach var="app" items="${apps}">
			*${app._appId}* <b>${app.status} </b>
					<a href="appdetail?appId=${app._appId}" title="quickview">Detailas</a>
					<p>
				</c:forEach>
			</c:if>
			<c:if test="${apps eq null}">no applications for this job now</c:if>
		</c:if>
	</center>
</body>
</html>