<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.JobBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Search Results</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<center><h2>--- Job Searching Results ---</h2></center>
	<center><c:if test="${jobs != null}">
		<c:forEach var="job" items="${jobs}">
			<c:out value="${job._jobId}" /><a href="detail?jobId=${job._jobId}" title="quickview">See Details</a>
			<p>
		</c:forEach>
	</c:if>
	<c:if test="${jobs == null}">no results</c:if></center>
</body>
</html>