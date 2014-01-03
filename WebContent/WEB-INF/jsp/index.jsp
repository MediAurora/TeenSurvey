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

<script type='text/javascript'>
function checkform()
{
	var x=document.forms["myForm"]["selectGrp"].value;
	if (x== null || x=="0")
	  {
	  alert("Select a group");
	  return false;
	  }

	return true;
}
</script>

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
com.teensurvey.business.UserList users 
				= (com.teensurvey.business.UserList)pageContext.findAttribute("userList");

System.out.println("JSP -- userID : ==>" + users.getUserID());
ArrayList group = users.getGroups();
if(group != null) {
	int len = group.size();
	System.out.println("JSP:  No. of groups ==>" + len);
%>
<center>
<form name="myForm" method="post" action="generateSurvey.htm" onSubmit="return checkform()">
Group Name:
<select name="selectGrp" id="grp">
<option value="0" selected>Select a group</option>
<%
	for(int i = 0; i < len; i++) {
		GroupInfo temp = (GroupInfo)group.get(i);
		int id = temp.getId();
		String grpName = temp.getGroupNO();
%>
  <option value="<%= id %>"><%= grpName %></option>
<%
	}
%>
</select>
<input type="submit" value="generate Results" align="center" style=" text-align: center; font-weight:bold; width: 150px; height: 50px">     
</form>
</center>

<%
}%>
<br>
<br> 
<table align="center" class="style12" style="width: 500px; height: 88px" border="2">
    <tr>
    <td colspan="100" class="style16">USER-DEVICE LIST</td>        
    </tr>
	<tr>
      	<td colspan="15" class="style14" valign="top" bgcolor="lightblue">User Code 
      	</td>
  	<td colspan="10" class="style14" valign="top" bgcolor="lightblue">Group Number
  	</td>
  	<td colspan="55" class="style14" valign="top" bgcolor="lightblue">Device ID
  	</td>
  	<td colspan="20" class="style14" valign="top" bgcolor="lightblue">User Survey
  	</td>
    </tr>
<%
ArrayList userArr = users.getUserList();
int size = 0;

if(userArr != null) {
	size = userArr.size();
	System.out.println("JSP -- userList size : ==>" + size);
}
for(int i=0; i < size; i ++){
	UserInfo tempUser = (UserInfo) userArr.get(i);
	String user_cd = tempUser.getUserID();
	String device_id = tempUser.getDeviceID();
	String group_no = tempUser.getGroupNO();
%>
  <form method="post" action="user.htm">
	<tr>
      	<td colspan="15" class="style17" valign="top" bgcolor="lightblue"><%= user_cd %>
      	    <input type="hidden" name="userID" value="<%= user_cd %>"> 
      	</td>
  		<td colspan="10" class="style17" valign="top" bgcolor="lightblue"><%= group_no %>
  		    <input type="hidden" name="groupNO" value="<%= group_no %>"> 
  		</td>
  		<td colspan="55" class="style17" valign="top" bgcolor="lightblue"><%= device_id %>
  		    <input type="hidden" name="deviceID" value="<%= device_id %>">
  		</td>
  		<td colspan="25" class="style14" valign="top" bgcolor="lightblue">
  		<input type="submit" value=" Check Survey" align="center" 
  							style=" text-align: center; font-weight:bold;">
    	</td>
    </tr>
   
    </form>
<%
}

%>

  </table>
</body>
</html>


