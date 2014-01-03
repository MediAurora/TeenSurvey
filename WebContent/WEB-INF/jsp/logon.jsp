
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Teen Survey Administration Site</title>
<style type="text/css">
.newStyle1 {
	background-color: #99CCFF;
	background-image: none;
	background-repeat: repeat;
	background-attachment: scroll;
	background-position: center center;
	border: medium ridge #808000;
}
.style1 {
	color: #800080;
}
.style2 {
	color: #000000;
}
.style4 {
	color: #000000;
	text-align: center;
}
.style5 {
	text-align: center;
	font-size: x-large;
}
.style6 {
	margin-top: 175px;
}
.style7 {
	font-size: small;
	text-align: right;
}
.style8 {
	margin-left: 40px;
}
.style9 {
	margin-left: 640px;
}
</style>
</head>
<body style="background-color: #D3EDEB">

<center>
<!--  
<p class="style5">Teen Survey Administration Site</p>
<br />
 -->
<form method="post">
<table width="25%" border="1">
	<tr>
		<td align="center" bgcolor="lightblue">Log on</td>
	</tr>
	<tr>
		<td>
		<table border="0" width="100%">
			<tr>
				<td width="33%" align="right">Username:</td>
				<td width="66%" align="left"><spring:bind
					path="credentials.username">
					<input type="text" name="username"
						value="<core:out value="${status.value}"/>" />
				</spring:bind></td>

			</tr>
			<tr>
				<td colspan="2" align="center"><spring:hasBindErrors
					name="credentials">
					<font color="red"><core:out value="${status.errorMessage}" /></font>
				</spring:hasBindErrors></td>
			</tr>
			<tr>
				<td width="33%" align="right">Password:</td>
				<td width="66%" align="left"><spring:bind
					path="credentials.password">
					<input type="password" name="password" />
				</spring:bind></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><spring:hasBindErrors
					name="credentials">
					<font color="red"><core:out value="${status.errorMessage}" /></font>
				</spring:hasBindErrors></td>
			</tr>
			<tr>
				<td align="center" colspan="2"><input type="submit"
					alignment="center" value="Logon"></td>
			</tr>
		</table>

		</td>
	</tr>
</table>

</form>

</center>

</body>
</html>