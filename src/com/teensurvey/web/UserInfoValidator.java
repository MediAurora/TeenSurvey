package com.teensurvey.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import com.teensurvey.business.Credentials;
import com.teensurvey.business.UserInfo;

public class UserInfoValidator implements Validator {
	
	private final Log logger = LogFactory.getLog(getClass());
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
	
	public void validate(Object obj, Errors errors) {
	  	logger.info("==========Inside UserInfoValidator -> validate method===========");
	    UserInfo userInfo = (UserInfo) obj;
	    if (userInfo == null) {
	        errors.rejectValue("username", "error.login.not-specified", null,
            "Value required.");
	    }
    	//String user_cd = (String)request.getAttribute("user_id");
	    String user_cd = userInfo.getUserID();
	    logger.info("user code from page ===>" + user_cd);
    	//String group_no = (String)request.getAttribute("group");
    	//logger.info("user's group number is ===>" + group_no + "==from command==>" + userInfo.getGroupNO());
    	//String device_id = (String)request.getAttribute("device_id");
    	//logger.info("user's devide ID is ===>" + device_id + "==from command==>" + userInfo.getDeviceID());

	}

	@Override
	public boolean supports(Class clazz) {
		// TODO Auto-generated method stub
		return clazz.equals(UserInfo.class);
	}

}
