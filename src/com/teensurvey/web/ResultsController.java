package com.teensurvey.web;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.teensurvey.business.GroupInfo;
import com.teensurvey.business.SurveyA;
import com.teensurvey.business.SurveyB;
import com.teensurvey.business.SurveyInfo;
import com.teensurvey.business.UserInfo;
import com.teensurvey.business.UserList;


public class ResultsController extends AbstractController{

   	protected final Log logger = LogFactory.getLog(getClass());
   	
    // Create a new application context. this processes the Spring config
	ApplicationContext ctx =
	    new ClassPathXmlApplicationContext("applicationContext.xml");
	
	// Retrieve the data source from the application context
	BasicDataSource ds = (BasicDataSource) ctx.getBean("dataSource");
	
	// Open a database connection using Spring's DataSourceUtils
	//Connection c = DataSourceUtils.getConnection(ds);
		
	//JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
	
	private String getGroupName(int id) {
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String group_no = null;
		try{
    	    // retrieve a list of users/password
    		String grpQuery = "select GROUP_NO from surveyserver.group where ID = " + id ;
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
    	    }
    	    
    	    if(size == 0) {
    	    	logger.error("Incorrect Group ID ..");

    	    }else if(size == 1) {
    	        group_no = rs.getString("GROUP_NO");
    	        logger.info("group_no ==>" + group_no);
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
        
        return group_no;
	}
	
	private void cleanSurveyData() {
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
    	    //clean Survey tables
			String cleanupQryA = "delete FROM surveyserver.SurveyA_Results";
			String cleanupQryB = "delete FROM surveyserver.SurveyB_Results";
    		logger.info("Prepared Sql Query ==>" + cleanupQryA);
    		logger.info("Prepared Sql Query ==>" + cleanupQryB);
    		
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
					ps = c.prepareStatement(cleanupQryA);
					int valA = ps.executeUpdate();
					logger.info("Nujmber of surveys deleted from SurveyA_Results==>" + valA);
					ps = c.prepareStatement(cleanupQryB);
					int valB = ps.executeUpdate();
					logger.info("Nujmber of surveys deleted from SurveyB_Results==>" + valB);
				}
			}
           	    
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
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		// TODO Auto-generated method stub
		logger.info("====== inside ResultsController -> onSubmit =======");
		
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		logger.info("loggedin user is ===>" + userId);
		session.setAttribute("userId", userId);

    	int group_Id = Integer.parseInt(request.getParameter("selectGrp"));
    	logger.info("user's group number is ===>" + group_Id);
    	GroupInfo groupInfo = new GroupInfo();
    	groupInfo.setId(group_Id);
    	String grpName = getGroupName(group_Id);
        logger.info("group name ==>" + grpName);
        groupInfo.setGroupNO(grpName);
        
        //clean all surveys parsed before
        cleanSurveyData();
        
    	try{
    		// retrieve a list of users and their answers
    		String srvQuery = "SELECT b.GROUP_NO, a.SURVEY_NAME, a.USER_ID, a.SURVEY_TIME, a.ANSWERS FROM surveyserver.survey_answers a,"
    						+ "Surveyserver.user b where a.user_id = b.user_id and b.group_no = " + group_Id ;
    		logger.info("Prepared Sql Query ==>" + srvQuery);
    		
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
					ps = c.prepareStatement(srvQuery);
					rs = ps.executeQuery();
				}
			}
    	    
     	    int size =0;  
    	    if (rs != null){  
    	      rs.beforeFirst();  
    	      rs.last();  
    	      size = rs.getRow();
    	      logger.info("Total number of surveys for current group ==>" + size);
    	      rs.beforeFirst();
    	    }
    	    
    	    if(size == 0) {
    	    	logger.error("No survey generated for selected group ..");

    	    }else {
    	    	while(rs.next()) {
    	    		SurveyInfo tempSurvey = new SurveyInfo();
    	    		int group_no = rs.getInt("GROUP_NO");
    	    		logger.info("group_no ==>" + group_no);
    	    		String surveyType = rs.getString("SURVEY_NAME");
    	    		logger.info("Survey Type ==>" + surveyType);
    	    		String user = rs.getString("USER_ID");
    	    		logger.info("user Id ==>" + user);
    	    		Date surveyTime = rs.getDate("SURVEY_TIME");
    	    		logger.info("Survey Time ==>" + surveyTime);
    	    		String answer = rs.getString("ANSWERS");
    	    		logger.info("Survey answers ==>" + answer);
    	    		tempSurvey.setGroupNo(group_no);
    	    		tempSurvey.setSurveyName(surveyType);
    	    		tempSurvey.setUserID(user);
    	    		tempSurvey.setSurveyTime(surveyTime);
    	    		tempSurvey.setAnswer(answer);
    	    		parseSurvey(tempSurvey);
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
    	return new ModelAndView("download", "groupInfo", groupInfo);
	}
	
	private void parseSurvey(SurveyInfo survey) {
		
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String answer = survey.getAnswer();
		String type = survey.getSurveyName();
		String userId = survey.getUserID();
		int group_no = survey.getGroupNo();
		Date surveyTime = survey.getSurveyTime();
		
		DocumentBuilder db;
		Document doc;
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
			    	String ans = element.getAttribute("ans");
			    	logger.info("===================");
			    	logger.info("Question is ===>" + qn);
			    	logger.info("Answer is ===>" + ans);
			    	String insertQueryA = "insert into surveyserver.SurveyA_Results (GROUP_NO, SURVEY_NAME, USER_ID, SURVEY_TIME, QUESTION, ANSWER)"
			    						+ "values (?, ?, ?,?, ?, ?)";
			    	logger.info("Prepared Sql Query ==>" + insertQueryA);
		
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
			    			ps = c.prepareStatement(insertQueryA);
			    			ps.setInt(1, group_no);
			    			ps.setString(2, type);
			    			ps.setString(3, userId);
			    			ps.setDate(4, (java.sql.Date) surveyTime);
			    			ps.setString(5, qn);
			    			ps.setString(6, ans);
			    			ps.execute();
			    		}
			    	}
		    	    ps.close();
		    	    ps = null;
		    	    c.close();
		    	    c = null;
				
			    } else if(surveyType.equals("SurveyB")) {
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
			    	String insertQueryB = "insert into surveyserver.SurveyB_Results (GROUP_NO, SURVEY_NAME, USER_ID, SURVEY_TIME, QUESTION, SERVING, OUNCES)"
						+ "values (?, ?, ?, ?, ?, ?, ?)";
			    	logger.info("Prepared Sql Query ==>" + insertQueryB);

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
			    			ps = c.prepareStatement(insertQueryB);
							ps.setInt(1, group_no);
							ps.setString(2, type);
							ps.setString(3, userId);
							ps.setDate(4, (java.sql.Date) surveyTime);
							ps.setString(5, qn);
							ps.setInt(6, serving);
							ps.setString(7, ounces);
							ps.execute();
			    		}
			    	}
		    	    ps.close();
		    	    ps = null;
		    	    c.close();
		    	    c = null;
			    }// end of SurveyB check
			}// end of for loop
	
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
        } 

	}// End ParseSurvey method
  
}// End of Class ResultsController
