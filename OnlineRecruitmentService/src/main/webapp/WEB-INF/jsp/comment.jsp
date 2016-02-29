<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.UserBean"%>
<%@ page import="com.ors.bean.JobBean"%>
<%@ page import="com.ors.bean.ApplicationBean"%>
<%@ page import="com.ors.bean.AutoCheckBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Review</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<h3>Comment for Application of Job ${job._jobId}
		${job.jobDescriptions}</h3>
	<form action='reviewResult' method='post'>
		<p>Comments:
		<p>
			<textarea name="comments" rows="4" cols="40" required="required"></textarea>
		<p>
			<input type="radio" name="decision" value="shortlisted" checked>
			Accept <input type="radio" name="decision" value="notshortlisted">
			Decline
		<p>
			<input type="hidden" name="appId" value="${app._appId}" /> <input
				type="hidden" name="jobId" value="${job._jobId}" /> <input
				type="submit" value="Submit" />
	</form>
</body>
</html>