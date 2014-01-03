package com.teensurvey.business;

import java.util.ArrayList;

public class UserList {
	
private String userID;
private String role;
@SuppressWarnings("rawtypes")
private ArrayList userList;
private ArrayList groups;

public void setUserID(String userID) {
	this.userID = userID;
}

public String getUserID() {
	return userID;
}

public void setUserList(ArrayList userList) {
	this.userList = userList;
}

public ArrayList getUserList() {
	return userList;
}

public void setGroups(ArrayList groups) {
	this.groups = groups;
}

public ArrayList getGroups() {
	return groups;
}

public void setRole(String role) {
	this.role = role;
}

public String getRole() {
	return role;
}

	
}