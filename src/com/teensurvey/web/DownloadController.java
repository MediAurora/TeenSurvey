package com.teensurvey.web;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Number;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

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


public class DownloadController<T> extends AbstractController{

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
	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;
	
	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
													HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		logger.info("====== inside DownLoadController -> onSubmit =======");
		
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("userId");
		logger.info("loggedin user is ===>" + userId);
		session.setAttribute("userId", userId);
    	
		String type = (String)request.getParameter("surveyType");
    	logger.info("Survey Type from page ===>" + type);
    	
		int group = Integer.parseInt((String)request.getParameter("groupNo"));
    	logger.info("Group selected is ===>" + group);

    	// Prepare to write Excel output
        ArrayList csv = new ArrayList();
    	
        ArrayList surveyData = getUserSurveyData(type, csv, group);
        
        String filename = "SurveyReport.csv";
        response.setHeader("content-type", "text/csv");
        response.setHeader("content-disposition", "attachment;filename=\"" + filename + "\"");
        writeCsv(surveyData, ',', response.getOutputStream());
        
        //setOutputFile("c:/temp/SurveyReport.xlsx");
   	   	//write();
   	   	//System.out.println("Please check the result file under c:/temp/SurveyReport.xls ");
        return new ModelAndView("survey", "surveyData", surveyData);
	}// end handleRequestInternal method
	
	
	public static <T> void writeCsv (ArrayList csv, char separator, OutputStream output) throws IOException {
	    
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
	    int size = csv.size();
		for (int i=0; i < size; i++) {
			List row =  (List)csv.get(i); 
			int len = row.size();
	        for (int j = 0; j < len; j++) {
	            String field = String.valueOf(row.get(j)).replace("\"", "\"\"");
	            if (field.indexOf(separator) > -1 || field.indexOf('"') > -1) {
	                field = '"' + field + '"';
	            }
	            writer.append(field);
	            if (row.iterator().hasNext()) {
	                writer.append(separator);
	            }
	        }
	        writer.newLine();
	    }
	    writer.flush();
	}
	
	
	public void write() throws IOException, WriteException {
	    File file = new File(inputFile);
	    WorkbookSettings wbSettings = new WorkbookSettings();

	    wbSettings.setLocale(new Locale("en", "EN"));

	    WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
	    workbook.createSheet("Report", 0);
	    WritableSheet excelSheet = workbook.getSheet(0);
	    createLabel(excelSheet);
	    createContent(excelSheet);

	    workbook.write();
	    workbook.close();
	}
	
	private void createLabel(WritableSheet sheet)
    						throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = 
			new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false, UnderlineStyle.SINGLE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		// Write a few headers
		addCaption(sheet, 0, 0, "Header 1");
		addCaption(sheet, 1, 0, "This is another header");
  

	}

	private void createContent(WritableSheet sheet) throws WriteException,
	    						RowsExceededException {
	  // Write a few number
	    for (int i = 1; i < 10; i++) {
	    // First column
	    addNumber(sheet, 0, i, i + 10);
	    // Second column
	    addNumber(sheet, 1, i, i * i);
	  }
	  // Lets calculate the sum of it
	  StringBuffer buf = new StringBuffer();
	  buf.append("SUM(A2:A10)");
	  Formula f = new Formula(0, 10, buf.toString());
	  sheet.addCell(f);
	  buf = new StringBuffer();
	  buf.append("SUM(B2:B10)");
	  f = new Formula(1, 10, buf.toString());
	  sheet.addCell(f);
	
	  // Now a bit of text
	  for (int i = 12; i < 20; i++) {
	    // First column
	    addLabel(sheet, 0, i, "Boring text " + i);
	    // Second column
	    addLabel(sheet, 1, i, "Another text");
	  }
	}
	
	private void addCaption(WritableSheet sheet, int column, int row, String s)
    								throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row,
						Integer integer) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, times);
		sheet.addCell(number);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
    								throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}

	/*public static void main(String[] args) throws WriteException, IOException {
		WriteExcel test = new WriteExcel();
		test.setOutputFile("c:/temp/lars.xls");
		test.write();
		System.out.println("Please check the result file under c:/temp/lars.xls ");
	}
	*/
	
	private ArrayList getUserSurveyData(String type, ArrayList csv, int group) {
		
		String psQuery = null;
		String grpStr = new Integer(group).toString();
		if(type == null) {
			logger.error("Survey Type is NULL. Creating default survey type as A...");
			type = "SurveyA";
		}
		
		if(type.equals("SurveyA")) {
			psQuery = "select USER_ID, SURVEY_TIME, QUESTION, ANSWER from surveyserver.surveya_results where GROUP_NO = "
				+ group;
	    	csv.add(Arrays.asList("GROUP_NO" ,"SURVEY_TYPE", "USER_ID","SURVEY_TIME", "QUESTION", "ANSWER"));
		}
		if(type.equals("SurveyB")) {
			psQuery = "select USER_ID, SURVEY_TIME, QUESTION, SERVING, OUNCES from surveyserver.surveyb_results where GROUP_NO = "
				+ group;
			csv.add(Arrays.asList("GROUP_NO" ,"SURVEY_TYPE", "USER_ID","SURVEY_TIME", "QUESTION", "SERVING", "OUNCE"));
		}
		try{
    	    // retrieve a list of users/password
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
    	      logger.info("Total number of surveys for this group ==>" + size);
    	      rs.beforeFirst(); 
    	    }
    	    
    	    if(size == 0) {
    	    	logger.error("No surveys recorded for this group==>" + group);
    	    }else {

    	    	while(rs.next()) {
    	    		String answer = null;
    	    		int serving = 0;
    	    		String ounce = null;
    	    		int userId = rs.getInt("USER_ID");
	    	        Date survey_time = (Date)rs.getObject("SURVEY_TIME");
	    	        String question = rs.getString("QUESTION");
	    	        
	    	        if(type.equals("SurveyA")){
	    	        	answer = rs.getString("ANSWER");
	    	        	String userStr = new Integer(userId).toString();
		    	        csv.add(Arrays.asList(grpStr, type, userStr, survey_time, question, answer));
	    	        }else if(type.equals("SurveyB")) {
	    	        	serving = rs.getInt("SERVING");
	    	        	String servingStr = new Integer(serving).toString();
		    	        ounce = rs.getString("OUNCES");	
		    	        csv.add(Arrays.asList(grpStr, type, userId,survey_time, question, servingStr, ounce));
	    	        }
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
	
		return csv;
	}
  
}
