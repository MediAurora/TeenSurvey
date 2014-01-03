package com.teensurvey.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.teensurvey.business.Credentials;

public class LogonValidator implements Validator {

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
	
    public boolean supports(Class clazz) {
        return clazz.equals(Credentials.class);
    }
    
    public void validate(Object obj, Errors errors) {
    	logger.info("==========Inside LogonValidator -> validate method===========");
        Credentials credentials = (Credentials) obj;
        if (credentials == null) {
            errors.rejectValue("username", "error.login.not-specified", null,
                    "Value required.");
        } else {

            logger.info("Validating user credentials for: "
                    + credentials.getUsername());
            if ((credentials.getUsername()== null)|| (credentials.getUsername().equals(""))) {
	       		logger.error("username is empty ..");
	       		errors.rejectValue("username", "error.login.not-specified",
                            null, "username is empty. try admin/admin");
            }
            if ((credentials.getPassword()== null)|| (credentials.getPassword().equals(""))) {
	       		logger.error("password is empty ..");
	       		errors.rejectValue("password", "error.login.not-specified",
                            null, "password is empty. try admin/admin");
            }

        	try{
        	    // retrieve a list of users/password
        		String psQuery = "select USER_ID, PASSWORD, ROLE from surveyserver.Survey_User" 
        							+ " where USER_ID = '"	+ credentials.getUsername() + "'";
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
        	    }
        	    
        	    if(size == 0) {
        	    	logger.error("Incorrect Username ..");
        	    	errors.rejectValue("username", "error.login.invalid-user",
                            null, "Incorrect Username.");
        	    }else if(size == 1) {
        	        String user = rs.getString("USER_ID");
        	        String password = rs.getString("PASSWORD");
        	        String role = rs.getString("ROLE");
        	        logger.info("username ==>" + user);
        	        logger.info("password ==>" + password);
        	        logger.info("role ==>" + role);
        	        
        	       	if (credentials.getPassword().equals(password) == false) {
        	       		logger.error("Incorrect password ..");
        	       		errors.rejectValue("password", "error.login.invalid-pass",
                                    null, "Incorrect Password.");
                    }else {
                    	logger.info("successful login!! ==>" + user);
                    }
        	        
        	    } else {
        	    	logger.error("more than one Users with same username ..");
        	    	errors.rejectValue("username", "error.login.more-user",
                            null, "more than one Users with same username ..");
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
            
        } // end else block
    }// end method: validate

}// end class: LogonValidator