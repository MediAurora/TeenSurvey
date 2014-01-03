package com.teensurvey.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.teensurvey.business.GroupInfo;
import com.teensurvey.business.UserInfo;
import com.teensurvey.business.UserList;


public class MasterController extends AbstractController{

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
	
	private ArrayList getGroups() {
		ArrayList groups = new ArrayList();
		
		try{
    	    // retrieve a list of users/password
    		String grpQuery = "select ID, GROUP_NO from surveyserver.group";
    		logger.info("Prepared Sql Query ==>" + grpQuery);
    		
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
					ps = c.prepareStatement(grpQuery);
					rs = ps.executeQuery();
				}
			}
    	    
     	    int size =0;  
    	    if (rs != null){  
    	      rs.beforeFirst();  
    	      rs.last();  
    	      size = rs.getRow();  
    	      logger.info("Total number of groups configured in system==>" + size);
    	      rs.beforeFirst(); 
    	    }
    	    
    	    if(size == 0) {
    	    	logger.error("No groups configured in system ..");
    	    }else {
    	    	
    	    	while(rs.next()) {
	    	        int Id = rs.getInt("ID");
	    	        String group = rs.getString("GROUP_NO");
	    	        GroupInfo grpInfo = new GroupInfo();
	    	        grpInfo.setId(Id);
	    	        grpInfo.setGroupNO(group);
	    	        groups.add(grpInfo);
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
		
		return groups;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private ArrayList getUserList() {

		ArrayList users = new ArrayList();
		
		try{
    	    // retrieve a list of users/password
    		String psQuery = "select USER_ID, GROUP_NO, DEVICE_ID from surveyserver.user";
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
    	      logger.info("Total number of participants configured in system==>" + size);
    	      rs.beforeFirst(); 
    	    }
    	    
    	    if(size == 0) {
    	    	logger.error("Incorrect Username ..");
    	    }else {
    	    	
    	    	while(rs.next()) {
	    	        String user_Id = rs.getString("USER_ID");
	    	        String group = rs.getString("GROUP_NO");
	    	        String device_Id = rs.getString("DEVICE_ID");
	    	        UserInfo userInfo = new UserInfo();
	    	        userInfo.setUserID(user_Id);
	    	        userInfo.setGroupNO(group);
	    	        userInfo.setDeviceID(device_Id);
	    	        users.add(userInfo);
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
		
		return users;
	}

	
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	private String getUserRole(String userID) {

		String role = null;
		
		try{
    	    // retrieve a list of users/password
    		String psQuery = "select ROLE from surveyserver.survey_user where USER_ID = '"
    							+ userID +"'" ;
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
    	      logger.info("Total number of site users ==>" + size);
    	      //rs.beforeFirst(); 
    	    }
    	    
    	    if(size == 0) {
    	    	logger.error("No Username in the system with ID==>" + userID);
    	    	
    	    }else if(size == 1){
    	    	
    	        role = rs.getString("ROLE");

    	    }else {
    	    	logger.error("more than 1 Username in the system with ID ==>" + userID);
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
		
		return role;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
  		logger.info("====== inside masterController-> handleRequestInternal =======");
		
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		logger.info("loggedin user is ===>" + userId);
		
    	session.setAttribute("userId", userId);
    	
    	UserList userListCmd = new UserList();
		
    	userListCmd.setUserID(userId);
    	
    	String role = getUserRole(userId);
    	userListCmd.setRole(role);
    	logger.info("user role is ===>" + role);
    	
    	if(role != null && role.equals("USER")) {
    		return new ModelAndView("downloadApp", "userList", userListCmd);
    	}
    	
    	@SuppressWarnings("rawtypes")
		ArrayList users = getUserList();
    	ArrayList groups = getGroups();
    	userListCmd.setUserList(users);
    	userListCmd.setGroups(groups);
    	
    	return new ModelAndView("index", "userList", userListCmd);

	}
  
}
