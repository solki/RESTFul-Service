<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.ors.bean.JobBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Apply For A Job</title>
</head>
<body>
	<jsp:include page="header.jsp" />
	<center>
		<h2>
			--- Apply for job <a href="detail?jobId=${job._jobId}">${job._jobId}</a> ---
		</h2>

		<form action='applyConfirmation' method='post'>
			<table cellspacing="25" border="0">
				<tr align="left">
					<td><h3><u>Personal Details:</h3></u></td>
				</tr>
				<tr>
					<td>Driving License Number: <input type="text"
						name="licenseNumber" required="required"
						placeholder="License Number" />
					</td>
				</tr>
				<tr>
					<td>Full Name: <input type="text" name="fullName"
						required="required" placeholder="Full Name" />
					</td>
				</tr>
				<tr>
					<td>Postcode: <input type="text" name="postcode"
						required="required" placeholder="Postcode" /></td>
				</tr>
				<tr>
					<td>-------------------------------------------------------</td>
				</tr>
				<tr>
					<td><h3><u>Cover Letter:</u></h3></td>
				</tr>
				<tr>
					<td><textarea name="coverLetter" rows="4" cols="40"
							required="required"></textarea></td>
				</tr>
				<tr>
					<td><h3><u>Brief Resume:</u></h3></td>
				</tr>
				<tr>
					<td><textarea name="briefResume" cols="40" rows="4"
							required="required"></textarea></td>
				</tr>
				<tr>
					<td><input type="hidden" name="jobId" value="${job._jobId}" />
						<input type="submit" value="Submit" /></td>
				</tr>
			</table>
		</form>
	</center>
</body>
</html>