<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.JobBean"%>
<%@ page import="com.ors.bean.ApplicationBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Application Form</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	Application ID:
	<b>${app._appId}</b>
	<p>
	<h3>Personal Details</h3>
	<form action='updateappconfirm' method='post'>
		<p>Driving License Number: ${app.licenseNumber}
		<p>
			Full Name: <input type="text" name="fullName" required="required"
				value="${app.fullName}" />
		<p>
			Postcode: <input type="text" name="postcode" required="required"
				value="${app.postcode}" />
		<p>---------------------------------------------------
		<p>Cover Letter:
		<p>
			<textarea name="coverLetter" rows="4" cols="40" required="required">${app.coverLetter}</textarea>
		<p>Brief Resume:
		<p>
			<textarea name="briefResume" cols="40" rows="4" required="required">${app.briefResume}</textarea>
		<p>
			<input type="hidden" name="appId" value="${app._appId}" /><input
				type="hidden" name="jobId" value="${app._jobId}" /><input
				type="hidden" name="licenseNumber" value="${app.licenseNumber}" /> <input
				type="submit" value="Submit" />
	</form>
</body>
</html>