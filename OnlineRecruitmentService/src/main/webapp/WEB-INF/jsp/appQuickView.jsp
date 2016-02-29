<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.UserBean"%>
<%@ page import="com.ors.bean.JobBean"%>
<%@ page import="com.ors.bean.ApplicationBean"%>
<%@ page import="com.ors.bean.ReviewBean"%>
<%@ page import="com.ors.bean.AutoCheckBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Application Quick View</title>
</head>
<body>

	<jsp:include page="header.jsp" />
	<center>
		<div>
			<h2>--- Job Information ---</h2>
			Job:
			<c:if test="${user.role eq 'manager'}">
				<a href="detail?jobId=${job._jobId}" title="quickview">${job._jobId}</a>
			</c:if>
			<c:if test="${(user eq null) or (user.role eq 'reviewer')}">
				<c:out value="${job._jobId}" />
			</c:if>
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
	</center>
	<center>
		<div>
			<h2>--- Application Details ---</h2>
			<p>
				<c:out value="Application ID: ${app._appId}" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Status:&nbsp;&nbsp;<font
					color="red"><c:out value="${app.status}" /></font>
			<p>
				<c:out value="Candidate: ${app.fullName}" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:out value="License Number: ${app.licenseNumber}" />
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<c:out value="Post Code: ${app.postcode}" />
			<p>
				<c:out value="Cover Letter: ${app.coverLetter}" />
			<p>
				<c:out value="Brief Resume: ${app.briefResume}" />
			<p>
				<c:if test="${(app.status eq 'created') and (user eq null)}">
					<a href="appUpdate?appId=${app._appId}">Update</a>
				</c:if>
				<c:if
					test="${((app.status eq 'created') or (app.status eq 'autochecked')) and (user eq null)}">
					<a href="appdeletion?appId=${app._appId}">Withdraw</a>
				</c:if>
				<c:if test="${(app.status eq 'acpt_notified') and (user eq null)}">
					<b>*Congratulations! You have been accepted.*</b>
				</c:if>
				<c:if test="${(app.status eq 'rjct_notified') and (user eq null)}">
					<b>*Sorry! You have been declined.*</b>
					<a href="index">Find more jobs</a>
				</c:if>
				<c:if
					test="${(user.role eq 'reviewer') and not (check eq null) and (app.status eq 'in_review')}">
					<a href="review?appId=${app._appId}">Review</a>
				</c:if>
				<c:if test="${(user eq null) and (app.status eq 'invited')}">
					<p>--Your application was short-listed. You have an interview
						invitation.--
					<p>
						*<a href="acceptinvite?appId=${app._appId}">Accept Invitation</a>*
						*<a href="rejectinvite?appId=${app._appId}">Reject Invitation</a>*

					
				</c:if>
				<c:if
					test="${(user.role eq 'manager') and (app.status eq 'itv_accepted')}">
					<a href="accept?appId=${app._appId}">Accept</a>
					<a href="reject?appId=${app._appId}">Reject</a>
				</c:if>
			<p>
		</div>
	</center>
	<center>
		<div>
			<h2>--- Auto Check Results ---</h2>
			<p>
				<c:if test="${check eq null}">
					no check results<p>
				</c:if>
				<c:if
					test="${(user.role eq 'manager') and (check eq null) and ((app.status eq 'created') or (app.status eq 'in_review'))}">
					<a
						href="autocheck?appId=${app._appId}&licenseNumber=${app.licenseNumber}&fullName=${app.fullName}&postcode=${app.postcode}">Run
						Check</a>
				</c:if>
				<c:if test="${not (check eq null)}">
					<pre>
						${check.resultDetails}
					</pre>
				</c:if>
		</div>
	</center>
</body>
</html>