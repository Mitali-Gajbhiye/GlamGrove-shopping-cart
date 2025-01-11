package com.e_com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_com.Entities.Cart;

public interface cartRep extends JpaRepository<Cart, Integer> {

	public Cart findByProductIdAndUserId(Integer productId, Integer usrId);

	public Integer countByUserId(Integer usrId);

	public List<Cart> findByUserId(Integer usrId);

}
