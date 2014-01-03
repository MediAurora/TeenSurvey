package com.teensurvey.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;

import com.teensurvey.business.SurveyInfo;
import com.teensurvey.business.UserInfo;
import com.teensurvey.business.UserList;


public class UserInfoController extends AbstractController{

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
		logger.info("====== inside UserInfoController -> onSubmit =======");
		
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		logger.info("loggedin user is ===>" + userId);
		session.setAttribute("userId", userId);
    	
			
    	UserInfo userInfoCmd = new UserInfo();
    	String user_cd = (String)request.getParameter("userID");
    	logger.info("user code from page ===>" + user_cd);
    	userInfoCmd.setUserID(user_cd);
    	String group_no = (String)request.getParameter("groupNO");
    	logger.info("user's group number is ===>" + group_no);
    	userInfoCmd.setGroupNO(group_no);
    	String device_id = (String)request.getParameter("deviceID");
    	logger.info("user's devide ID is ===>" + device_id);
    	userInfoCmd.setDeviceID(device_id);
    	ArrayList surveyList = getUserSurveyList(user_cd);
    	userInfoCmd.setSurveyList(surveyList);
    	return new ModelAndView("survey", "userInfo", userInfoCmd);
	}
	
	private ArrayList getUserSurveyList(String userId) {
		
		ArrayList surveys = new ArrayList();
		try{
    	    // retrieve a list of users/password
    		String psQuery = "select SURVEY_NAME, ANSWERS, SURVEY_TIME from surveyserver.survey_answers where USER_ID = '"
    							+ userId +"'";
    		logger.info("Prepared Sql Query ==>" + psQuery);
    		
    		c = ds.getConnection();
    		if(c!= null && c.isClosed()) {
				logger.error("Connection is CLOSED!");
				c = ds.getConnection();
			} else {
				if(c == null) {
					logger.error("Connection is NULL. Creating new connection object...");
					c = ds.getConnection();
				}else {
					logger.info("Connection is active.. creating prepared statement and executing query");
					ps = c.prepareStatement(psQuery);
					rs = ps.executeQuery();
				}
			}
    	    
     	    int size =0;  
    	    if (rs != null){  
    	      rs.beforeFirst();  
    	      rs.last();  
    	      size = rs.getRow();  
    	      logger.info("Total number of surveys for this user ==>" + size);
    	      rs.beforeFirst(); 
    	    }
    	    
    	    if(size == 0) {
    	    	logger.error("Incorrect Username ..");
    	    }else {
    	    	
    	    	while(rs.next()) {
	    	        String survey_name = rs.getString("SURVEY_NAME");
	    	        String answer = rs.getString("ANSWERS");
	    	        Date survey_time = (Date)rs.getObject("SURVEY_TIME");
	    	        SurveyInfo surveyInfo = new SurveyInfo();
	    	        surveyInfo.setSurveyName(survey_name);
	    	        surveyInfo.setAnswer(answer);
	    	        surveyInfo.setSurveyTime(survey_time);
	    	        surveys.add(surveyInfo);
    	    	}
    	    }
    	    rs.close();
    	    rs = null;
    	    ps.close();
    	    ps = null;
    	    c.close();
    	    c = null;
    	} catch (SQLException ex) {
    	    // something has failed and we print a stack trace to analyze the error
    	    ex.printStackTrace();
    	    // ignore failure closing connection
    	    //try { c.close(); } catch (SQLException e) {e.printStackTrace(); }
        }finally {
            // Always make sure result sets and statements are closed,
            // and the connection is returned to the pool
            if (rs != null) {
              try { rs.close(); } catch (SQLException e) { ; }
              rs = null;
            }
            
            if (ps != null) {
              try { ps.close(); } catch (SQLException e) { ; }
              ps = null;
            }
            
            if (c != null) {
              try { c.close(); } catch (SQLException e) { ; }
              c = null;
            }
        } //end finally
	
		return surveys;
	}
  
}
