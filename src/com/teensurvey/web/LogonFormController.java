package com.teensurvey.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import com.teensurvey.business.Credentials;

public class LogonFormController extends SimpleFormController {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	public ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response,
            Object command,
            BindException errors) throws Exception {
		
		logger.info("====== Inside LogonFormController-> onSubmit method =======");
        
		Credentials credentials = (Credentials) command;
		String user = credentials.getUsername();
		logger.info("<LogonFormController> username==>:" + user);
		
		HttpSession session = request.getSession();
    	session.setAttribute("userId", user);
    	
    	logger.info("<LogonFormController> Redirecting to==>:" 
				+ new RedirectView(getSuccessView()));
		return new ModelAndView(new RedirectView(getSuccessView()));
    }

}
