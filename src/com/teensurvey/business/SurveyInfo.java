package com.teensurvey.business;

import java.util.ArrayList;
import java.util.Date;

public class SurveyInfo {
	private int groupNo;
	private String surveyName;
	private String answer;
	private Date surveyTime;
	private String userID;
	private ArrayList SurveyA;
	private ArrayList SurveyB;
	
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}
	public String getSurveyName() {
		return surveyName;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswer() {
		return answer;
	}
	public void setSurveyTime(Date surveyTime) {
		this.surveyTime = surveyTime;
	}
	public Date getSurveyTime() {
		return surveyTime;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserID() {
		return userID;
	}
	public void setSurveyA(ArrayList surveyA) {
		SurveyA = surveyA;
	}
	public ArrayList getSurveyA() {
		return SurveyA;
	}
	public void setSurveyB(ArrayList surveyB) {
		SurveyB = surveyB;
	}
	public ArrayList getSurveyB() {
		return SurveyB;
	}
	public void setGroupNo(int groupNo) {
		this.groupNo = groupNo;
	}
	public int getGroupNo() {
		return groupNo;
	}

	
}