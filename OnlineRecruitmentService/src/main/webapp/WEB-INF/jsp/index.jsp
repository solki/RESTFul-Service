<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome to ORS</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<p>
	<p>
	<center>
		<h3>--- Search For Jobs ---</h3>
	</center>
	<center>
		<form action='search' method='post'>
		<table cellspacing="20" border="0">
			<tr><td>
				<p style="color:red"><c:if test="${keyword=='no'}">
					* Key Words is needed.
				</c:if></p>
				Key Words: <input type="text" name="keyWord" required="required"
					placeholder="keyWord" /></td>
			</tr>
			<tr><td>
			
				Salary: <select name="salary">
					<option value="0" selected="selected">0+</option>
					<option value="500">500+</option>
					<option value="1000">1k+</option>
					<option value="2000">2k+</option>
					<option value="5000">5k+</option>
					<option value="10000">10k+</option>
					<option value="50000">50k+</option>
				</select>
			</td></tr>
			<tr><td>
			
				Work type: <select name="positionType">
					<option value="Full" selected="selected">Full Time</option>
					<option value="Part">Part Time</option>
					<option value="Contract">Contract</option>
					<option value="Temp">Temporary</option>
					<option value="Casual">Casual</option>
				</select>
			</td></tr>
			<tr><td>
				Where: <select name="location">
					<option value="ACT" selected="selected">ACT</option>
					<option value="NSW">NSW</option>
					<option value="NT">NT</option>
					<option value="Qld">Qld</option>
					<option value="SA">SA</option>
					<option value="Tas">Tas</option>
					<option value="Vic">Vic</option>
					<option value="WA">WA</option>
				</select>
			</td></tr>
			
			</table>
				<input type="submit" value="Search" />
		</form>
	</center>

</body>
</html>