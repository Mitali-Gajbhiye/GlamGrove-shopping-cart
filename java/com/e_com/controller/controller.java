package com.e_com.controller;

import java.io.File;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.e_com.Entities.Product;
import com.e_com.Entities.UserReg;
import com.e_com.Entities.category;
import com.e_com.repository.cartRep;
import com.e_com.service.CartService;
import com.e_com.service.CatService;
import com.e_com.service.productService;
import com.e_com.service.userService;
import com.e_com.util.CommonUtil;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(method = RequestMethod.POST)
public class controller {
	 
	@Autowired
    private CatService catServ;
	@Autowired
    private productService proServ;
	@Autowired
    private userService userSer;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private cartRep cartResp;
	
	@Autowired
	private CartService cartService;
	

	
	@ModelAttribute
	public void getUsrDetails(Principal p, Model m)
	{  
		if(p != null)
		{
		String email = p.getName();
		UserReg usrDet = userSer.getUsrByEmail(email);
		m.addAttribute("user",usrDet);
		Integer countCart = cartService.getCountCart(usrDet.getId());
		m.addAttribute("countCart", countCart);
		}
		List<category> allCat = catServ.getAllActiveCat();
		m.addAttribute("category", allCat);
		
		
	}

	@GetMapping("/")
	public String index()
	{
		
		return "index";
	}
	@GetMapping( "/slogin")
	public String loginn()
	{	
		return "loginn";
	}
	@GetMapping("/register")
	public String register()
	{	
		return "register";
	}
	
	@GetMapping("/butterflies")
	public String butterflies(Model m, String category)
	{
		List<category> categories  = catServ.getAllActiveCat();
		List<Product> products = proServ.getAllActiveFlies(category);
		m.addAttribute("categories", categories);
		m.addAttribute("products",products);
		
		return "butterflies";
	}
	
	@GetMapping("/viewDetails/{id}")
	public String viewDetails(@PathVariable int id, Model m)
	{
		Product product = proServ.getFliesById(id);
		m.addAttribute("p",product);
		return "viewDetails";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserReg usr, @RequestParam("img") MultipartFile file, HttpSession ses) throws IOException
	{
	String imageName = file.isEmpty()? "Default.jpg" : file.getOriginalFilename();
	usr.setProfileImg(imageName);
	UserReg  saveusr = userSer.saveUser(usr);
	if(!ObjectUtils.isEmpty(saveusr))
	{
	if(!file.isEmpty())
	{
		File saveFile = new ClassPathResource("static/image").getFile();
	    Path path = Paths.get(saveFile.getAbsolutePath() +File.separator + "ProfileImg" 
		+File.separator + file.getOriginalFilename());
	    System.out.println(path);
	    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	 }
	 ses.setAttribute("succMSG", "Registerd successfully");
	}else {	 ses.setAttribute("errorMsg", "Registerd  failed");}

	
     return"redirect:/register";	
	}
	
	
	@GetMapping("/reset-Password")
	public String resetPass(@RequestParam String token, HttpSession session, Model m)
	{
		UserReg userByToken = userSer.getUsrByToken(token);

		if (userByToken == null) {
			m.addAttribute("Msg", "Your link is invalid or expired !!");
			return "sendMessagehtml";
		}
		m.addAttribute("token", token);
		return"resetPassword.html";
	}
	
	@PostMapping("/resetPassword")
	public String SendResetPassword(@RequestParam String token, @RequestParam String password, HttpSession session,
			Model m) {

		UserReg userByToken = userSer.getUsrByToken(token);
		if (userByToken == null) {
			m.addAttribute("errorMsg", "Your link is invalid or expired !!");
			return "sendMessagehtml";
		} else {
			userByToken.setPassword(passwordEncoder.encode(password));
			userByToken.setResetToken(null);
			userSer.updateUser(userByToken);
			// session.setAttribute("succMsg", "Password change successfully");
			m.addAttribute("Msg", "Password change successfully");

			return "sendMessagehtml";
					
		         }
		}
	@GetMapping("/forgot-Password")
	public String forgotPass()
	{
		return"forgotPassword.html";
	}
	   
	
	@PostMapping("/forgotPassword")//"/forgot-Password" putting this i was getting ambiguity so changed path
	public String sendForgotPass(@RequestParam String email, HttpSession session, HttpServletRequest req) throws UnsupportedEncodingException, MessagingException
	{
		UserReg usrByMail = userSer.getUsrByEmail(email);
		if (ObjectUtils.isEmpty(usrByMail)) {
			session.setAttribute("errorMsg", "Invalid email");
		} else {
			  String resetToken = UUID.randomUUID().toString(); 
			  userSer.updateUserResetToken(email, resetToken);
			 
			  // Generate URL : http://localhost:8080/resetPassword?token=sfgdbgfswegfbdgfewgvsrg
			  
			  String url = CommonUtil.generateUrl(req) + "/reset-Password?token=" +resetToken;
			 
			
			  Boolean sendMail = commonUtil.sendMail(url, email);

			if (sendMail) {
				session.setAttribute("succMSG", "Please check your email..Password Reset link sent");
			} else {
				session.setAttribute("errorMsg", "Somethong wrong on server ! Email not send");
			}
		}
		return "redirect:/forgot-Password";
	}
} 
























