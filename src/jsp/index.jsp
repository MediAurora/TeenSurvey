<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Teen Survey App</title>
<%@ include file="/WEB-INF/jsp/include.jsp"%>	
<Script type="text/javascript">
function validateForm() {
	var x=document.forms["login"]["user"].value;
	var y=document.forms["login"]["pwd"].value;
	if (x == null || x == "") {
		alert("User Id must be filled out.");
		return false;
	}
	if (y == null || y == "") {
		alert("Password must be filled out.");
		return false;
	}
	return true;
}

</script>	
</head>

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
	font-weight:bold;
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
.style10 {
	font-size: medium;
	color: #000000;
}
.style11 {
	margin-bottom: 9px;
}
.style12 {
	border: 1px solid #800080;
	background-color: #808080;
	background-position: center center;
}

.style13 {
	color: #0000FF;
}
.style14 {
	text-align: center;
}

.style15{
	text-align: center;
	font-size: large;
	font-weight:normal;
}

.style16{
	text-align: center;
	font-size: medium;
	font-weight:normal;
}

.styleSubmit {
	text-align: center;
	font-size: x-large;
	font-weight:bold;
}
</style>

</head>
  <body style="background-color: #D3EDEB">
  	<p class="style8" style="height: 22px"><em class="style9">
	</p>
<p class="style5">Teen Survey Administration Site</p>

<hr class="style4" style="height: -11px" />
  	<p class="style8" style="height: 22px"><em class="style9">
	</p>


<p class="style15">Welcome to Administration site for Teen Survey Project.</p>
</br>
</br> 
<form name="login" method="post" action="loginAction.jsp" onsubmit="return validateForm()">
<table align="center" class="style12" style="width: 600px; height: 88px" border="2">
     <tr>
        <td colspan="100" style=" text-align: center; font-weight:bold;">Administrator Login</td>        
     </tr>
	<tr>
      	<td colspan="50" class="style14" valign="top" bgcolor="lightblue">User Id :
		<input type="text" name="user"></td>
  	<td colspan="50" class="style14" valign="top" bgcolor="lightblue">Password :
		<input type="password" name="pwd"></td>
	</tr>
</table>
<?php
if(isset($_SESSION['success'])) {
	//echo "Success flag is set as==>".$_SESSION['success'];
   if(!$_SESSION['success']) {
	
?>
<p class="style14" style="color: Red"> Invalid login! Please check UserId or Password and try again. </p>
<?php
	unset($_SESSION['success']);
   }
}
?>
<p class="styleSubmit"><input type="submit" value="Login">
</p>
</form>
<br>
	<br>
		
<p class="style16">Note* This service is for Chronic Kidney Disease (CKD) patients who are not on dialysis or transplant patients.</p>
</br>
  </body>
</html>



