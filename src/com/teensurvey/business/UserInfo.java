package com.teensurvey.business;

import java.util.ArrayList;

public class UserInfo {
	private String userID;
	private String groupNO;
	private String deviceID;
	private ArrayList surveyList;
	private SurveyInfo surveyInfo;
	
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserID() {
		return userID;
	}
	public void setGroupNO(String groupNO) {
		this.groupNO = groupNO;
	}
	public String getGroupNO() {
		return groupNO;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setSurveyList(ArrayList surveyList) {
		this.surveyList = surveyList;
	}
	public ArrayList getSurveyList() {
		return surveyList;
	}
	public void setSurveyInfo(SurveyInfo surveyInfo) {
		this.surveyInfo = surveyInfo;
	}
	public SurveyInfo getSurveyInfo() {
		return surveyInfo;
	}

	
	
}