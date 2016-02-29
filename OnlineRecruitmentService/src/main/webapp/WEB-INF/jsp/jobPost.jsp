<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.UserBean"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Post Job Form</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<c:if test="${(not empty user) and not (user.role eq manager)}">

		<form action="postjob" method="post">
			ClosingDate: <input type="date" name="closingDate"
				required="required" placeholder="yyyy-mm-dd"> Location: <select
				name="location">
				<option value="ACT" selected="selected">ACT</option>
				<option value="NSW">NSW</option>
				<option value="NT">NT</option>
				<option value="Qld">Qld</option>
				<option value="SA">SA</option>
				<option value="Tas">Tas</option>
				<option value="Vic">Vic</option>
				<option value="WA">WA</option>
			</select> Work type: <select name="positionType">
				<option value="Full Time" selected="selected">Full Time</option>
				<option value="Part Time">Part Time</option>
				<option value="Contract">Contract</option>
				<option value="Temporary">Temporary</option>
				<option value="Casual">Casual</option>
			</select><p> Department: <select name="department">
				<option value="AIMS Project" selected="selected">AIMS
					Project</option>
				<option value="SAMBA Project">SAMBA Project</option>
			</select> Salary: <input type="text" name="salary" required="required"
				placeholder="10000" /> '0' means Negotiable.
			<p>JobDescriptions:
			<p>
				<textarea name="jobDescriptions" rows="4" cols="40"></textarea>
			<p>
				<input type="submit" value="Submit" />
		</form>
	</c:if>
</body>
</html>