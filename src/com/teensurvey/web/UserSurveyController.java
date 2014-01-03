package com.teensurvey.web;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.teensurvey.business.SurveyA;
import com.teensurvey.business.SurveyB;
import com.teensurvey.business.SurveyInfo;
import com.teensurvey.business.UserInfo;
import com.teensurvey.business.UserList;


public class UserSurveyController extends AbstractController{

   	protected final Log logger = LogFactory.getLog(getClass());
   	
    // Create a new application context. this processes the Spring config
	ApplicationContext ctx =
	    new ClassPathXmlApplicationContext("applicationContext.xml");
	
	// Retrieve the data source from the application context
	BasicDataSource ds = (BasicDataSource) ctx.getBean("dataSource");
	
	Connection c = null;
	// Open a database connection using Spring's DataSourceUtils
	//Connection c = DataSourceUtils.getConnection(ds);
		
	//JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
	
	PreparedStatement ps = null;
	ResultSet rs = null;
   	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		logger.info("====== inside UserSurveyController -> onSubmit =======");
		
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		logger.info("loggedin user is ===>" + userId);
		session.setAttribute("userId", userId);
    	
			
    	UserInfo userInfoCmd = new UserInfo();
    	String user_cd = (String)request.getParameter("user_id");
    	logger.info("user code from page ===>" + user_cd);
    	userInfoCmd.setUserID(user_cd);
    	String group_no = (String)request.getParameter("group");
    	logger.info("user's group number is ===>" + group_no);
    	userInfoCmd.setGroupNO(group_no);
    	String device_id = (String)request.getParameter("device_id");
    	logger.info("user's devide ID is ===>" + device_id);
    	userInfoCmd.setDeviceID(device_id);
    	String answer = (String)request.getParameter("answer");
    	logger.info("user's answer is ===>" + answer);
    	String surveyType = (String)request.getParameter("surveyType");
    	logger.info("Survey Tpye is ===>" + surveyType);
    	String surveyTime = (String)request.getParameter("surveyTime");
    	logger.info("Survey Time is ===>" + surveyTime);
		Date surveyTimeStamp = null;
    	try {
    		DateFormat formatter ; 
     		formatter = new SimpleDateFormat("yyyy-d-M H:m:s.S");
    		surveyTimeStamp = (Date)formatter.parse(surveyTime);  
    		System.out.println("Today is " + surveyTimeStamp );
    	} catch (ParseException e){
    		System.out.println("Exception :"+e);  
    	}  
		
    	SurveyInfo survey = new SurveyInfo();
    	survey.setAnswer(answer);
    	survey.setUserID(user_cd);
    	survey.setSurveyName(surveyType);
    	survey.setSurveyTime(surveyTimeStamp);
    	
    	SurveyInfo surveyObj = parseSurvey(survey);
    	userInfoCmd.setSurveyInfo(surveyObj);
    	return new ModelAndView("surveyAnswers", "userInfo", userInfoCmd);
	}
	
	private SurveyInfo parseSurvey(SurveyInfo survey) {
		
		String answer = survey.getAnswer();
		String type = survey.getSurveyName();
		
		DocumentBuilder db;
		Document doc;
		ArrayList SurveyAList = new ArrayList ();
		ArrayList SurveyBList = new ArrayList ();
		try {
		    InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(answer));
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("Result");
			Element resElement = (Element)nodes.item(0);
			String surveyType = resElement.getAttribute("name");
			logger.info("Survey type ===>" + surveyType);
			String surveyUser = resElement.getAttribute("user");
			logger.info("Survey User ===>" + surveyUser);
			NodeList surveyQ = resElement.getElementsByTagName("each");
				
			for (int i = 0; i < surveyQ.getLength(); i++) {
				Element element = (Element) surveyQ.item(i);
			    String qn = element.getAttribute("qn");
			    if(surveyType.equals("SurveyA")) {
					SurveyA tempA = new SurveyA();
			    	String ans = element.getAttribute("ans");
			    	logger.info("===================");
			    	logger.info("Question is ===>" + qn);
			    	logger.info("Answer is ===>" + ans);
			    	tempA.setQuestion(qn);
			    	tempA.setAnswer(ans);
			    	SurveyAList.add(tempA);
				
			    } else if(surveyType.equals("SurveyB")) {
			    	SurveyB tempB = new SurveyB();
			    	String tmp = element.getAttribute("servings");
			    	int serving = 0;
			    	if(!tmp.equals("")) {
			    		serving = Integer.parseInt(tmp);
			    	}
			    	String ounces = element.getAttribute("ounces");
			    	logger.info("===================");
			    	logger.info("Question is ===>" + qn);
			    	logger.info("serving is ===>" + serving);
			    	logger.info("ounces is ===>" + ounces);
			    	tempB.setQuestion(qn);
			    	tempB.setServings(serving);
			    	tempB.setOunces(ounces);
			    	SurveyBList.add(tempB);
			    }
			    
			}
			survey.setSurveyA(SurveyAList);
			survey.setSurveyB(SurveyBList);
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return survey;
	}
  
}
