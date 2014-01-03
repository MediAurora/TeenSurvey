<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->
<html>
<head>
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Teen Survey Administration Site</title>
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
	font-weight:bold;
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
.style10 {
	font-size: medium;
	color: #000000;
}
.style11 {
	margin-bottom: 9px;
}
.style12 {
	border: 1px solid #800080;
	background-color: grey;
	background-position: center center;
}

.style13 {
	color: #0000FF;
}
.style14 {
	text-align: center;
	font-weight:bold;
	font-size: medium;
}

.style15{
	text-align: center;
	font-size: large;
	font-weight:normal;
}

.style16{
	text-align: center;
	font-size: medium;
	font-weight:bold;
}
.smallFont {
	font-size: small;
	text-align: left;
}
.styleSubmit {
	text-align: center;
	font-size: x-large;
	font-weight:bold;
}

.style17 {
	text-align: center;
	font-weight: normal;
	font-size: medium;
}
.style18 {
	text-align: left;
	font-weight: normal;
	font-size: medium;
}
</style>

</head>
<!-- <body style="background-color: #D3EDEB">
  	<p class="style8" style="height: 22px"><em class="style9">
	</p>
<p class="style5">Teen Survey Administration Site</p>

<hr class="style4" style="height: -11px" />
  	<p class="style8" style="height: 22px"><em class="style9">
	</p>


<p class="style15">Welcome to Administration site for Teen Survey Project.</p>
<br>
 -->  
<%
com.teensurvey.business.UserInfo userInfo 
= (com.teensurvey.business.UserInfo)pageContext.findAttribute("userInfo");
ArrayList surveys = userInfo.getSurveyList();
System.out.println("JSP -- userID : ==>" + userInfo.getUserID());
String userID = userInfo.getUserID();
String group = userInfo.getGroupNO();
String deviceID = userInfo.getDeviceID();
SurveyInfo survey = userInfo.getSurveyInfo();
String type = survey.getSurveyName();
java.util.Date surveyTime	= survey.getSurveyTime();
%>
<form method="post" action="home.htm">
<input type="submit" value="Back to HOME" align="center" style=" text-align: center; font-weight:bold; width: 100px; height: 50px">     
</form>

<table align="center" class="style12" style="width: 500px; height: 88px" border="2">
    <tr>
    <td colspan="50" class="style16">USER-SURVEY LIST</td>        
    </tr>
    <tr>
    	<td colspan="50" class="style18" valign="top" bgcolor="lightgrey">User: <%= userID %> 
      	</td>
     </tr>
     <tr>
  		<td colspan="50" class="style18" valign="top" bgcolor="lightgrey">Group: <%= group %>
  		</td>
  	</tr>
     <tr>
  		<td colspan="50" class="style18" valign="top" bgcolor="lightgrey">Device: <%= deviceID %>
  		</td>
    </tr>
    <tr>
  		<td colspan="50" class="style18" valign="top" bgcolor="lightgrey">Survey Type: <%= type %>
  		</td>
    </tr>
    <tr>
  		<td colspan="50" class="style18" valign="top" bgcolor="lightgrey">Survey Time: <%= surveyTime %>
  		</td>
    </tr>
<%
ArrayList qnsA = survey.getSurveyA();
ArrayList qnsB = survey.getSurveyB();
if(type.equals("SurveyA")){
%>
	<tr>
      	<td colspan="35" class="style14" valign="top" bgcolor="lightblue">Question 
      	</td>
  		<td colspan="15" class="style14" valign="top" bgcolor="lightblue">Answer
  		</td>
    </tr>
<%
} else if(type.equals("SurveyB")){
%>
	<tr>
      	<td colspan="25" class="style14" valign="top" bgcolor="lightblue">Question 
      	</td>
  		<td colspan="10" class="style14" valign="top" bgcolor="lightblue">Servings
  		</td>
  		<td colspan="15" class="style14" valign="top" bgcolor="lightblue">Ounces
  		</td>
    </tr>
<%} %>

<%

int size = 0;
if(type.equals("SurveyA")){
	if(qnsA != null) {
		size = qnsA.size();
		System.out.println("JSP -- SurveyA Question size : ==>" + size);
	}
	for(int i=0; i < size; i ++){
		SurveyA q = (SurveyA) qnsA.get(i);
		String surveyQ = q.getQuestion();
		String answer = q.getAnswer();

%>
	<tr>
      	<td colspan="35" class="style17" valign="top" bgcolor="lightblue"><%= surveyQ %> 
      	</td>
  		<td colspan="15" class="style17" valign="top" bgcolor="lightblue"><%= answer %>
  		</td>
    </tr>

<%
	}
}else if(type.equals("SurveyB")) {
	if(qnsB != null) {
		size = qnsB.size();
		System.out.println("JSP -- SurveyB Question size : ==>" + size);
	}
	for(int i=0; i < size; i ++){
		SurveyB q = (SurveyB) qnsB.get(i);
		String surveyQ = q.getQuestion();
		int serving = q.getServings();
		String ounce = q.getOunces();

%>
	<tr>
      	<td colspan="25" class="style17" valign="top" bgcolor="lightblue"><%= surveyQ %> 
      	</td>
  		<td colspan="10" class="style17" valign="top" bgcolor="lightblue"><%= serving %>
  		</td>
  		<td colspan="15" class="style17" valign="top" bgcolor="lightblue"><%= ounce %>
  		</td>
    </tr>
<%	
	}
}

%>

  </table>
</body>
</html>


