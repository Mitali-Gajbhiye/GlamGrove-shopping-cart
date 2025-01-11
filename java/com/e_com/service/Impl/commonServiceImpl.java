package com.e_com.service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.e_com.service.commonService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class commonServiceImpl implements  commonService{

	@Override
	public void removeSession() {
	 HttpServletRequest req	= ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes()))
			 .getRequest(); 
		
	 HttpSession ses = req.getSession();
	 ses.removeAttribute("succMSG");
	 ses.removeAttribute("errorMsg");
	 
	}
	

} 
