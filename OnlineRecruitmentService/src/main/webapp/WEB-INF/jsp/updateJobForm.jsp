<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Job Form</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<form action="updatejobconfirm" method="post">
		Job ID: <b>${job._jobId}</b>
		<p>
			ClosingDate: <input type="date" id="datepicker2" name="closingDate"
				value="${job.closingDate}" required="required"
				placeholder="yyyy-mm-dd"> Location: <select name="location">
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
			</select>Department: <select name="department">
				<option value="AIMS Project" selected="selected">AIMS
					Project</option>
				<option value="SAMBA Project">SAMBA Project</option>
			</select> Salary: <input type="text" name="salary" value="${job.salary}"
				required="required" placeholder="10000" />
		<p>
			JobDescriptions:
			<textarea name="jobDescriptions" rows="4" cols="40">${job.jobDescriptions}</textarea>
		<p>
			<input type="hidden" name="jobId" value="${job._jobId}"> <input
				type="submit" value="Submit" />
	</form>
</body>
</html>