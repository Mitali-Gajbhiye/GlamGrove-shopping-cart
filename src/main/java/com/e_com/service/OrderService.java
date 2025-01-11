package com.e_com.service;

import java.util.List;

import com.e_com.Entities.OrderAddress;
import com.e_com.Entities.OrderDetails;
import com.e_com.Entities.OrderReq;

public interface OrderService {

	public void saveOrder(Integer userid, OrderReq orderRequest) throws Exception;

	public List<OrderDetails> getOrdersByUser(Integer userId);

	public OrderDetails updateOrderStatus(Integer id, String status);

	public List<OrderDetails> getAllOrders();

	public OrderDetails getOrdersByOrderId(String orderId);
	
	//public Page<OrderDetails> getAllOrdersPagination(Integer pageNo,Integer pageSize);
}
