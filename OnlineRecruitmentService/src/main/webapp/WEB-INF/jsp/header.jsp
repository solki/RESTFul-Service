<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.UserBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<c:if test="${user eq null}">
		<center>
			<h1>ONLINE RECRUITMENT SERVICE</h1>
		</center>
		<c:if test="${login=='failed'}">
	login ${login}! Please try again.
	</c:if>
		<c:if test="${uidpwd=='no'}">
	ID and Password are needed.
	</c:if>
		<center>
			<form action='login' method='post'>
				ID: <input type="text" name="uid" value="" required="required" />
				&nbsp;&nbsp;&nbsp;&nbsp;Password: <input type="password" name="pwd"
					value="" required="required" /> <input type="submit" value="Login" />
			</form>
		</center>
		<p>
		<hr>
		<div align="left">
			<a href="home">Search Job</a>
		</div>
		<p>
	</c:if>
	<c:if test="${user.role eq 'reviewer'}">
		<center>
			<h1>ONLINE RECRUITMENT SERVICE</h1>
			<b>*Reviewer*</b> ${user.firstName} ${user.lastName} <a href="logout">Logout</a>
			<p>
			<hr>
			<div align="left">
				<a href="home">Home</a>
			</div>
			<p>
		</center>
	</c:if>
	<c:if test="${user.role eq 'manager'}">
		<center>
			<h1>ONLINE RECRUITMENT SERVICE</h1>
			<b>*Manager*</b> ${user.firstName} ${user.lastName} <a href="logout">Logout</a>
			<p>
		</center>
		<hr>
		<div align="left">
			<a href="home">Home</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="applications">Applications</a>&nbsp;&nbsp;&nbsp;&nbsp;<a
				href="postjobform">Post Job</a>&nbsp;&nbsp;&nbsp;&nbsp;<a
				href="index">Search Jobs</a>
			<p>
		</div>
		<p>
	</c:if>
</body>
</html>