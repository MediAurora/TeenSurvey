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
%>
<form method="post" action="home.htm">
<input type="submit" value="Back to HOME" align="center" style=" text-align: center; font-weight:bold; width: 110px; height: 50px">     
</form>

<table align="center" class="style12" style="width: 500px; height: 88px" border="2">
    <tr>
    <td colspan="50" class="style16">USER-SURVEY LIST</td>        
    </tr>
    <tr>
    	<td colspan="50" class="style18" valign="top" bgcolor="grey">User: <%= userID %> 
      	</td>
     </tr>
     <tr>
  		<td colspan="50" class="style18" valign="top" bgcolor="grey">Group: <%= group %>
  		</td>
  	</tr>
     <tr>
  		<td colspan="50" class="style18" valign="top" bgcolor="grey">Device: <%= deviceID %>
  		</td>
    </tr>
	<tr>
      	<td colspan="15" class="style14" valign="top" bgcolor="lightblue">Survey Name 
      	</td>
  		<td colspan="20" class="style14" valign="top" bgcolor="lightblue">Survey Time
  		</td>
  		<td colspan="15" class="style14" valign="top" bgcolor="lightblue">Survey Answer
  		</td>
    </tr>
<%
int size = 0;

if(surveys != null) {
	size = surveys.size();
	System.out.println("JSP -- SurveyList size : ==>" + size);
}
for(int i=0; i < size; i ++){
	SurveyInfo tempSurvey = (SurveyInfo) surveys.get(i);
	String surveyName = tempSurvey.getSurveyName();
	String answer = tempSurvey.getAnswer();
	String surveyTime = tempSurvey.getSurveyTime().toString();
	System.out.println("JSP -- Survey Answers : ==>" + answer);
%>
  <form method="post" action="survey.htm">
	<tr>
      	<td colspan="15" class="style17" valign="top" bgcolor="lightblue"><%= surveyName %> 
      	</td>
  		<td colspan="20" class="style17" valign="top" bgcolor="lightblue"><%= surveyTime %>
  		</td>
 		<td colspan="15" class="style14" valign="top" bgcolor="lightblue">
  		<input type="submit" value=" View Answers" align="center" 
  							style=" text-align: center; font-weight:bold;">
    	</td>
    	<td>
    		<input type="hidden" name="user_id" value="<%= userID %>">
    		<input type="hidden" name="group" value="<%= group %>">
    		<input type="hidden" name="device_id" value="<%= deviceID %>">
    		<input type="hidden" name="answer" value="<%= answer %>">
    		<input type="hidden" name="surveyType" value="<%= surveyName %>">
    		<input type="hidden" name="surveyTime" value="<%= surveyTime %>">
    	</td>
    </tr>
    </form>
<%

}

%>

  </table>
</body>
</html>


