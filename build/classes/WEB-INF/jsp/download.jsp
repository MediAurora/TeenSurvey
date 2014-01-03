
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
 <form method="post" action="home.htm">
<input type="submit" value="Home" align="center" style=" text-align: center; font-weight:bold; width: 150px; height: 50px">     
</form>

<center>
<!--  
<p class="style5">Teen Survey Administration Site</p>
<br />
 -->
 <%
 com.teensurvey.business.GroupInfo group 
 = (com.teensurvey.business.GroupInfo)pageContext.findAttribute("groupInfo");
 String grpName = null;
 int groupNo = 0;
 if(group != null) {
	 grpName = group.getGroupNO();
	 groupNo = group.getId();
 }
	 
 %>
 Group Selected: <%= grpName %>
 <br />
 <form method="post" action="download.htm">
<input type="submit" value="generate SurveyA" align="center" style=" text-align: center; font-weight:bold; width: 150px; height: 50px">
<input type="hidden" name="surveyType" value="SurveyA">
 <input type="hidden" name="groupNo" value="<%= groupNo %>">    
</form>
<br />
<form method="post" action="download.htm">
<input type="submit" value="generate SurveyB" align="center" style=" text-align: center; font-weight:bold; width: 150px; height: 50px">
<input type="hidden" name="surveyType" value="SurveyB">
 <input type="hidden" name="groupNo" value="<%= groupNo %>">     
</form>

</center>

</body>
</html>