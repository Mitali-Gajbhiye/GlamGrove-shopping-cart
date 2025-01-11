package com.e_com.service.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.e_com.Entities.Cart;
import com.e_com.Entities.Product;
import com.e_com.Entities.UserReg;
import com.e_com.repository.cartRep;
import com.e_com.repository.productRespository;
import com.e_com.repository.userResp;
import com.e_com.service.CartService;

@Service
public class CartServiceImpl  implements CartService{

	
	  @Autowired 
	   private cartRep cartRep;
	  @Autowired
	   private userResp usrRep;
	  @Autowired
	   private productRespository productRep;
	 
	
	@Override
	public Cart saveCart(Integer productId, Integer usrId) {
		UserReg user = usrRep.findById(usrId).get();
		Product product = productRep.findById(productId).get();

		Cart cartStatus = cartRep.findByProductIdAndUserId(productId, usrId);

		Cart cart = null;

		if (ObjectUtils.isEmpty(cartStatus)) {
			cart = new Cart();
			cart.setProduct(product);
			cart.setUser(user);
			cart.setQuantity(1);
			cart.setTotalPrice(1 * product.getDiscountPrice());
		} else {
			cart = cartStatus;
			cart.setQuantity(cart.getQuantity() + 1);
			cart.setTotalPrice(cart.getQuantity() * cart.getProduct().getDiscountPrice());
		}
		Cart saveCart = cartRep.save(cart);

		return saveCart;
	}

	
	  @Override
	  public List<Cart> getCartsByUser(Integer usrId)
	  { 
		  List<Cart> carts= cartRep.findByUserId(usrId);
	  
			Double totalOrderPrice = 0.0;
			List<Cart> updateCarts = new ArrayList<>();
			for (Cart c : carts) 
			{
				Double totalPrice = (c.getProduct().getDiscountPrice() * c.getQuantity());
				c.setTotalPrice(totalPrice);
				totalOrderPrice = totalOrderPrice + totalPrice;
				c.setTotalOrderPrice(totalOrderPrice);
				updateCarts.add(c);
			}

			return updateCarts;
	  }
	  
	  @Override public Integer getCountCart(Integer usrId) 
	  { 
		  Integer countByUserId= cartRep.countByUserId(usrId);
		  return countByUserId;
	  }
	  
	  @Override public void updateQuantity(String sy, Integer cid) { 
	  
	        Cart cart = cartRep.findById(cid).get();
		    int updateQuantity;
		    
			if (sy.equalsIgnoreCase("de")) {
				updateQuantity = cart.getQuantity() - 1;

				if (updateQuantity <= 0) {
					cartRep.delete(cart);
				} else {
					cart.setQuantity(updateQuantity);
					cartRep.save(cart);
				}
			}
			else {
				updateQuantity = cart.getQuantity() + 1;
				cart.setQuantity(updateQuantity);
				cartRep.save(cart);
			}
            
			
			
		}
	  
	 
}
