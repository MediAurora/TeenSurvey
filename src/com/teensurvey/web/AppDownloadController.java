package com.teensurvey.web;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AppDownloadController<T> extends AbstractController{

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
		logger.info("====== inside AppDownLoadController -> onSubmit =======");
		
		String fileType = (String)request.getParameter("fileType");
		logger.info("File type is ===>" + fileType);
		
		response.setContentType("application/octet-stream");
		
		File file = null;

		
		if(fileType == null) {
			logger.error("File type is null. redirecting to same page");
			return new ModelAndView("downloadApp");
		}else if (fileType.equals("mobileprovision")){
			FileInputStream in = null;
			ServletOutputStream out = null;
			response.setHeader("Content-Disposition",
									"attachment;filename=TeenageSurveyDistrProfile.mobileprovision");
			
			try{
				file = new File("C:\\downloads\\Teen_Survey\\TeenageSurveyDistrProfile.mobileprovision");
				logger.info("File created ===>" + file);
				
				in = new FileInputStream(file);
				out = response.getOutputStream();
				int len = (int)file.length();
				byte[] content = new byte[len];
				
				while(in.read(content, 0, len) != -1){
					out.write(content, 0, len);
				}
			} finally {
				in.close();
				out.flush();
				out.close();
			}
			//ServletContext sc = request.getSession().getServletContext();
			//String mimetype = sc.getMimeType(file.getName());
		}else if(fileType.equals("ipa")) {
			response.setHeader("Content-Disposition",
									"attachment;filename=TeenSurveyApp.ipa");
			FileInputStream in = null;
			ServletOutputStream out = null;
			try{
				file = new File("C:\\downloads\\Teen_Survey\\TeenSurveyApp.ipa");
	
				logger.info("File created ===>" + file);
				
				in = new FileInputStream(file);
				out = response.getOutputStream();
				int len = (int)file.length();
				byte[] content = new byte[len];
				
				while(in.read(content, 0, len) != -1){
					out.write(content, 0, len);
				}
			} finally {
				in.close();
				out.flush();
				out.close();
			}
			//ServletContext sc = request.getSession().getServletContext();
			//String mimetype = sc.getMimeType(file.getName());
		}
		
        return new ModelAndView("downloadApp");
	}// end handleRequestInternal method
	
  
}
