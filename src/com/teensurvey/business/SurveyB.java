package com.teensurvey.business;

import java.util.ArrayList;

public class SurveyB {
	
	private String question;
	private int servings; 
	private String ounces;
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getQuestion() {
		return question;
	}
	public void setOunces(String ounces) {
		this.ounces = ounces;
	}
	public String getOunces() {
		return ounces;
	}
	public void setServings(int servings) {
		this.servings = servings;
	}
	public int getServings() {
		return servings;
	}
	
}