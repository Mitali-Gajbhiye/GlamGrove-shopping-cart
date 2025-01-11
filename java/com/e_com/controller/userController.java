package com.e_com.controller;

import java.security.Principal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.e_com.Entities.Cart;
import com.e_com.Entities.OrderDetails;
import com.e_com.Entities.OrderReq;
import com.e_com.Entities.UserReg;
import com.e_com.Entities.category;
import com.e_com.service.CartService;
import com.e_com.service.CatService;
import com.e_com.service.OrderService;
import com.e_com.service.userService;
import com.e_com.util.CommonUtil;
import com.e_com.util.OrderStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/users", method = RequestMethod.GET)
public class userController {
	
	@Autowired
    private userService userSer;
	@Autowired
	private CatService categoryService;
	@Autowired
	private CartService cartService;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private OrderService  orderService;
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
		List<category> allCat = categoryService.getAllActiveCat();
		m.addAttribute("category", allCat);
		
	}

	@GetMapping("/") 
	public String home()
	{
	   return"users/home";	
	}
	
	
	@GetMapping("/addCart")
	public String addToCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session) {
		Cart saveCart = cartService.saveCart(pid, uid);

		if (ObjectUtils.isEmpty(saveCart)) {
			session.setAttribute("errorMsg", "Product add to cart failed");
		} else {
			session.setAttribute("succMSG", "Product added to cart");
		}
		return "redirect:/viewDetails/" + pid;
	}
	
	
	private UserReg getLoggedInUserDetails(Principal p) {
		String email = p.getName();
		UserReg userDtls = userSer.getUsrByEmail(email);
		return userDtls;
	}
	@GetMapping("/Tcart")
	public String totalCart(Principal p, Model m) {

		UserReg user = getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		m.addAttribute("carts", carts);
		if (carts.size() > 0) {
			Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
			m.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		return "/users/cart";
	}
	
	@GetMapping("/cartQuantityUpdate")
	public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid) {
		cartService.updateQuantity(sy, cid);
		return "redirect:/users/Tcart";
	}

	@GetMapping("/orders")
	public String orderPage(Principal p, Model m) {
		UserReg user = getLoggedInUserDetails(p);
		List<Cart> carts = cartService.getCartsByUser(user.getId());
		m.addAttribute("carts", carts);
		if (carts.size() > 0) {
			Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
			Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice() + 250 + 100;
			m.addAttribute("orderPrice", orderPrice);
			m.addAttribute("totalOrderPrice", totalOrderPrice);
		}
		return "/users/orders_his";
	}
	
	@PostMapping("/save-order")
	public String saveOrder(@ModelAttribute OrderReq  request, Principal p) throws Exception {
		 System.out.println(request);
		UserReg user = getLoggedInUserDetails(p);
		orderService.saveOrder(user.getId(), request);

		return "redirect:/users/orders";
	}
	
	@GetMapping("/userOrders")
	public String myOrder(Model m, Principal p) {
		UserReg loginUser = getLoggedInUserDetails(p);
		List<OrderDetails> orders = orderService.getOrdersByUser(loginUser.getId());
		m.addAttribute("orders", orders);
		return "/users/myOrders";
	}
	@GetMapping("/updateStatus")
	public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

		OrderStatus[] values = OrderStatus.values();
		String status = null;

		for (OrderStatus orderSt : values) {
			if (orderSt.getId().equals(st)) {
				status = orderSt.getName();
			}
		}

		OrderDetails updateOrder = orderService.updateOrderStatus(id, status);
		
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
		return "redirect:/users/userOrders";
	}

	
	@GetMapping("/profile")
	public String profile() {
		return "/users/profile";
	}
	
	@PostMapping("/updateProfile")
	public String updateProfile(@ModelAttribute UserReg user, @RequestParam MultipartFile img, HttpSession session) {
		UserReg updateUserProfile = userSer.updateUserProfile(user, img);
		if (ObjectUtils.isEmpty(updateUserProfile)) {
			session.setAttribute("errorMsg", "Profile not updated");
		} else {
			session.setAttribute("succMSG", "Profile Updated");
		}
		return "redirect:/users/profile";
	}

	@PostMapping("/changePassword")
	public String changePassword(@RequestParam String newPassword, @RequestParam String currentPassword, Principal p,
			HttpSession session) {
		UserReg loggedInUserDetails = getLoggedInUserDetails(p);

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

		return "redirect:/users/profile";
	}

	
	
}
