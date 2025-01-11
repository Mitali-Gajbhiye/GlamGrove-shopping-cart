package com.e_com.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.UUID;

import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.e_com.Entities.OrderDetails;
import com.e_com.Entities.UserReg;
import com.e_com.service.userService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class CommonUtil {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private userService userService;
	

	public Boolean sendMail(String url, String reciepentEmail) throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("daspabitra55@gmail.com", "Shooping Cart");
		helper.setTo(reciepentEmail);

		String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
				+ "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
				+ "\">Change my password</a></p>";
		helper.setSubject("Password Reset");
		helper.setText(content, true);
		mailSender.send(message);
		return true;
	}

	public static String generateUrl(HttpServletRequest request) {

		// http://localhost:8080/forgot-password
		/*
		 * String siteUrl = request.getRequestURL().toString();
		 * 
		 * return siteUrl.replace(request.getServletPath(), "");
		 */
		
		String scheme = request.getScheme();  // "http" or "https"
	    
	    // Get the server name (e.g., localhost or your domain)
	    String serverName = request.getServerName();  // "localhost" or production domain
	    
	    // Set the port to 8080 (as you've fixed the port)
	    int port = 8080;  // Fixed port number
	    
	    // Build the base URL (http://localhost:8080 or https://yourdomain.com:8080)
	    String baseUrl = scheme + "://" + serverName + ":" + port;
	    
	    return baseUrl;
	}
	
	public Boolean sendMailForProductOrder(OrderDetails order,String status) throws Exception
	{
		
		String msg="<p>Hello [[name]],</p>"
				+ "<p>Thank you order <b>[[orderStatus]]</b>.</p>"
				+ "<p><b>Product Details:</b></p>"
				+ "<p>Name : [[productName]]</p>"
				+ "<p>Category : [[category]]</p>"
				+ "<p>Quantity : [[quantity]]</p>"
				+ "<p>Price : [[price]]</p>"
				+ "<p>Payment Type : [[paymentType]]</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom("aarahihu.7229@gmail.com", "Shooping Cart");
		helper.setTo(order.getOrderAddress().getEmail());

		msg=msg.replace("[[name]]",order.getOrderAddress().getFirstName());
		msg=msg.replace("[[orderStatus]]",status);
		msg=msg.replace("[[productName]]", order.getProduct().getTitle());
		msg=msg.replace("[[category]]", order.getProduct().getCategory());
		msg=msg.replace("[[quantity]]", order.getQuantity().toString());
		msg=msg.replace("[[price]]", order.getPrice().toString());
		msg=msg.replace("[[paymentType]]", order.getPaymentType());
		
		helper.setSubject("Product Order Status");
		helper.setText(msg, true);
		mailSender.send(message);
		return true;
	}
	
	public UserReg getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserReg userDtls = userService.getUsrByEmail(email);
		return userDtls;
	}

	
}