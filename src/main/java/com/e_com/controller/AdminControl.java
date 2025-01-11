package com.e_com.controller;



import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.e_com.Entities.OrderDetails;
import com.e_com.Entities.Product;
import com.e_com.Entities.UserReg;
import com.e_com.Entities.Categories;
import com.e_com.Entities.Category;
import com.e_com.service.CartService;
import com.e_com.service.CatService;
import com.e_com.service.OrderService;
import com.e_com.service.productService;
import com.e_com.service.userService;
import com.e_com.util.CommonUtil;
import com.e_com.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/admin", method = RequestMethod.GET)
public class AdminControl {	
	@Autowired
	private CatService categoryService;  
	@Autowired
	private productService productServ;
	@Autowired
    private userService userSer;
	@Autowired
	private CartService cartService;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private OrderService orderServ;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
		List<Categories> allCat = categoryService.getAllActiveCat();
		m.addAttribute("category", allCat);
		
	}
	
	@GetMapping("/")
	public String index(){
		
		return"admin/index";
	}
	
	@GetMapping("/categoryy")
	public String categoryy(Model m){
		m.addAttribute("catgs",categoryService.getAllCat());
		return"admin/category";
	}
	
	@PostMapping("/saveCat")
	public String saveCat(@ModelAttribute  Categories cat, @RequestParam MultipartFile file, HttpSession session,BindingResult result) throws IOException//responsive message
	{
		if (result.hasErrors()) {
	        System.out.println("Binding Errors: " + result.getAllErrors());
	        session.setAttribute("errorMsg", "Binding errors occurred.");
	        return "redirect:/admin/categoryy";
	    }
		
		 if (cat == null) {
		        session.setAttribute("errorMsg", "Category object is null");
		        return "redirect:/admin/categoryy";
		    }
		
		
		String imageName = file != null ? file.getOriginalFilename(): "Default.jpg";
		cat.setImageName(imageName);
		
		Boolean  catEx = categoryService.existCategory(cat.getName());//category exist
		if(catEx)
		{
			session.setAttribute("errorMsg", "Category name already exist");
		}	else {
			Categories savCategory = categoryService.saveCategory(cat);
			  if(ObjectUtils.isEmpty(savCategory)){
				      session.setAttribute("errorMsg", "Not saved! internal server error");
			     } else { 
			                 File saveFile = new ClassPathResource("static/image").getFile();
				             Path  path = Paths.get(saveFile.getAbsolutePath()+File.separator+"cat"+File.separator+file.getOriginalFilename());
				             System.out.println("Hey Path: " +path);
				             Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			    	         session.setAttribute("succMSG", "saved successfully");
			    	    }
		}
		
		return"redirect:/admin/categoryy";
	}
	
	
	@GetMapping("/deleteCat/{id}")
	public String deleteCat(@PathVariable int id, HttpSession session){
		Boolean deleteCat = categoryService.deleteCat(id);
		if(deleteCat){
			session.setAttribute("succMSG", "Category succefully get deleted");
		}
		else {
			session.setAttribute("errorMsg", "Something went wrong on server");
		}
		return"redirect:/admin/categoryy";
	}
	
	@GetMapping("/loadEditCat/{id}")
	public String loadEditCat(@PathVariable int id, Model m){
		m.addAttribute("catId",categoryService.getCatId(id));
		return"admin/edit_cat";
	}
	
	@PostMapping("/updateCat")
	public String updateCat(@ModelAttribute Categories cat, @RequestParam MultipartFile file, HttpSession ses) throws IOException
	{
		Categories oldCat = categoryService.getCatId(cat.getId());
		String imageName = file.isEmpty() ? oldCat.getImageName() : file.getOriginalFilename();
		if(!ObjectUtils.isEmpty(cat))
		{
			oldCat.setImageName(cat.getName());
			oldCat.setIsActive(cat.getIsActive());
			oldCat.setImageName(imageName);
		}
		Categories updateCa = categoryService.saveCategory(oldCat);
		 if(!ObjectUtils.isEmpty(updateCa)) {
			 if(!file.isEmpty())
			 {
				 File saveFile = new ClassPathResource("static/image").getFile();
				 Path  path = Paths.get(saveFile.getAbsolutePath()+File.separator+"cat"+File.separator+
						 file.getOriginalFilename());
				 Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    	         
			 }
			 ses.setAttribute("succMSG", "Category get updated");
		 }
		 else ses.setAttribute("errorMsg", "Internal Server Error");
		
		return"redirect:/admin/categoryy";    //loadEditCat/"+ cat.getId();
	}
	
	@GetMapping("/addFlies")
	public String addFlies(Model m){
		List<Categories> Categories = categoryService.getAllCat();
		m.addAttribute("categories",Categories);
		return"admin/add_flies";
	}
	
	@PostMapping("/saveFlies")
	public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
			HttpSession session) throws IOException {

		String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

		product.setImage(imageName);
		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());
		Product saveProduct = productServ.saveProduct(product);

		if (!ObjectUtils.isEmpty(saveProduct)) {

			File saveFile = new ClassPathResource("static/image").getFile();

			Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "productImg" + File.separator
					+ image.getOriginalFilename());

			// System.out.println(path);
			Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

			session.setAttribute("succMSG", "Product Saved Success");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/admin/addFlies";
	}
	
	 @GetMapping("/editFlies/{id}")
		public String editFlies(@PathVariable int id, Model m)
		{
	    	
	    	m.addAttribute("product", productServ.getFliesById(id));
	    	m.addAttribute("categories", categoryService.getAllCat());
			return"/admin/edit_prod";
		}
	
	@GetMapping("/viewFlies")
	public String viewFlies(Model m)
	{
		m.addAttribute("product", productServ.getAllFlies());
		return"admin/flies";
	}
	
	@PostMapping("/updateFlies")
	public String updateProduct(@ModelAttribute Product product,HttpSession ses , @RequestParam MultipartFile file ,Model m)
	{
		if(product.getDiscount() < 0 || product.getDiscount() >100)
		{
			ses.setAttribute("errorMsg", "Invalid Input");
		}
		else {
		Product updateProduct = productServ.updateProduct(product, file);
		if(!ObjectUtils.isEmpty(updateProduct)){
			ses.setAttribute("succMSG", "product update");
		}else {ses.setAttribute("error", "Internal server ");}
		}
		
		return"redirect:/admin/editFlies/"+product.getId();
	}
	
	@GetMapping("/deleteFlies/{id}")
	public String deleteFlies(@PathVariable int id, HttpSession ses)
	{
		Boolean deleteFlies =  productServ.deleteFlies(id);
		
		if(deleteFlies)
		{
			ses.setAttribute("succMSG", "Flies get deleted succefully");
		}
		else ses.setAttribute("errorMsg", "Internal server error");
		return"redirect:/admin/viewFlies";
	}
	
	
	
	@GetMapping("/users")
	public String getAllUsers(Model m,@RequestParam Integer type)
	{
		List<UserReg> users = null;
		if(type == 1) {
		users = userSer.getUsers("ROLE_USER");
	    }
	else {
		users = userSer.getUsers("ROLE_ADMIN");
	}
		m.addAttribute("userType", type);
		m.addAttribute("users", users);
		return "admin/users";
	}
	@GetMapping("/adm")
	public String getAllAdmin(Model m)
	{
		List<UserReg> adm = userSer.getUsers("ROLE_ADMIN");
		m.addAttribute("admin", adm);
		return "admin/adm";
	}
	@GetMapping("/updateSt")
	public String updateUsrAccStatus(@RequestParam Boolean status, @RequestParam Integer id,@RequestParam Integer type, HttpSession session)
	{
		Boolean st = userSer.updateAccStatus(id, status);
		if(st)
		{session.setAttribute("succMSG", "Updated");}
		else {session.setAttribute("errorMsg", "Error");}
		return "redirect:/admin/users?type="+type;
	}
	
	@GetMapping("/orders")
	public String orders(Model m) {
		List<OrderDetails > allOrders = orderServ .getAllOrders();
		m.addAttribute("orders", allOrders);
		return"admin/orders";
	}
	
	@PostMapping("/update-order-status")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

		OrderStatus[] values = OrderStatus.values();
		String status = null;

		for (OrderStatus orderSt : values) {
			if (orderSt.getId().equals(st)) {
				status = orderSt.getName();
			}
		}

	   OrderDetails updateOrder = orderServ.updateOrderStatus(id, status);

		try {
			commonUtil.sendMailForProductOrder(updateOrder, status);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!ObjectUtils.isEmpty(updateOrder)) {
			session.setAttribute("succMSG", "Status Updated");
		} else {
			session.setAttribute("errorMsg", "status not updated");
		}
		return "redirect:/admin/orders";
	}
	
	@GetMapping("/AdminOrders")
	public String AdminOrders(@RequestParam String orderId, Model m, HttpSession session) {

		

			OrderDetails order = orderServ.getOrdersByOrderId(orderId.trim());

			if (ObjectUtils.isEmpty(order)) {
				session.setAttribute("errorMsg", "Incorrect orderId");
				m.addAttribute("orderDtls", null);
			} else {
				m.addAttribute("orderDtls", order);
			}

		
		return "admin/orders";

	}

	
	@GetMapping("/addAdm")
	public String addAdm(){
		
		return"admin/add_adm";
	}
	@PostMapping("/saveAdmin")
	public String saveAdmin(@ModelAttribute UserReg user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
		user.setProfileImg(imageName);
		UserReg saveUser = userSer.saveAdmin(user);

		if (!ObjectUtils.isEmpty(saveUser)) {
			if (!file.isEmpty()) {
				File saveFile = new ClassPathResource("static/image").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profileImg" + File.separator
						+ file.getOriginalFilename());

//				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			session.setAttribute("succMSG", "Register successfully");
		} else {
			session.setAttribute("errorMsg", "something wrong on server");
		}

		return "redirect:/admin/addAdm";
	}
		
	@GetMapping("/profile")
	public String profile(){
		
		return"admin/profile";
	}
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute UserReg user, @RequestParam MultipartFile img, HttpSession session) {
		UserReg updateUserProfile = userSer.updateUserProfile(user, img);
		if (ObjectUtils.isEmpty(updateUserProfile)) {
			session.setAttribute("errorMsg", "Profile not updated");
		} else {
			session.setAttribute("succMSG", "Profile Updated");
		}
		return "redirect:/admin/profile";
	}

	@PostMapping("/changePassword")
	public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
			HttpSession session) {
		UserReg loggedInUserDetails = commonUtil.getLoggedInUserDetails(p);

		boolean matches = passwordEncoder.matches(currentPassword, loggedInUserDetails.getPassword());

		if (matches) {
			String encodePassword = passwordEncoder.encode(newPassword);
			loggedInUserDetails.setPassword(encodePassword);
			UserReg updateUser = userSer.updateUser(loggedInUserDetails);
			if (ObjectUtils.isEmpty(updateUser)) {
				session.setAttribute("errorMsg", "Password not updated !! Error in server");
			} else {
				session.setAttribute("succMSG", "Password Updated sucessfully");
			}
		} else {
			session.setAttribute("errorMsg", "Current Password incorrect");
		}

		return "redirect:/admin/profile";
	}


}
