package com.e_com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_com.Entities.OrderDetails;

public interface FliesOrderRep extends JpaRepository<OrderDetails, Integer>{
	List<OrderDetails> findByUserId(Integer userId);

	OrderDetails findByOrderId(String orderId);

}
