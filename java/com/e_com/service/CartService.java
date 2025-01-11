package com.e_com.service;

import java.util.List;

import com.e_com.Entities.Cart;


public interface CartService {

	public Cart saveCart(Integer fliesId, Integer usrId);
    public List<Cart> getCartsByUser(Integer usrId);
	public Integer getCountCart(Integer usrId);
    public void updateQuantity(String sy, Integer cid);	

}
