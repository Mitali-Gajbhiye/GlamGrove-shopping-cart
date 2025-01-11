package com.e_com.service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.e_com.Entities.Cart;
import com.e_com.Entities.OrderAddress;
import com.e_com.Entities.OrderDetails;
import com.e_com.Entities.OrderReq;
import com.e_com.repository.FliesOrderRep;
import com.e_com.repository.cartRep;
import com.e_com.repository.productRespository;
import com.e_com.service.OrderService;
import com.e_com.util.CommonUtil;
import com.e_com.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private FliesOrderRep orderRepository;

	@Autowired
	private cartRep cartRepository;

	@Autowired
	private CommonUtil commonUtil;

	

	

	@Override
	public OrderDetails updateOrderStatus(Integer id, String status) {
		Optional<OrderDetails> findById = orderRepository.findById(id);
		if (findById.isPresent()) {
			OrderDetails productOrder = findById.get();
			productOrder.setStatus(status);
			OrderDetails updateOrder = orderRepository.save(productOrder);
			return updateOrder;
		}
		return null;
		
	}

	@Override
	public List<OrderDetails> getAllOrders() {
		return orderRepository.findAll();
	}

	@Override
	public OrderDetails getOrdersByOrderId(String orderId) {
		
		return orderRepository.findByOrderId(orderId);
	}

	@Override
	public void saveOrder(Integer userid, OrderReq orderRequest) throws Exception {
		List<Cart> carts = cartRepository.findByUserId(userid);

		for (Cart cart : carts) {

			OrderDetails order = new OrderDetails();

			order.setOrderId(UUID.randomUUID().toString());
			order.setOrderDate(LocalDate.now());

			order.setProduct(cart.getProduct());
			order.setPrice(cart.getProduct().getDiscountPrice());

			order.setQuantity(cart.getQuantity());
			order.setUser(cart.getUser());

			order.setStatus(OrderStatus.IN_PROGRESS.getName());
			order.setPaymentType(orderRequest.getPaymentType());

			OrderAddress address = new OrderAddress();
			address.setFirstName(orderRequest.getFirstName());
			address.setLastName(orderRequest.getLastName());
			address.setEmail(orderRequest.getEmail());
			address.setMobileNo(orderRequest.getMobileNo());
			address.setAddress(orderRequest.getAddress());
			address.setCity(orderRequest.getCity());
			address.setState(orderRequest.getState());
			address.setPincode(orderRequest.getPincode());

			order.setOrderAddress(address);

			OrderDetails saveOrder = orderRepository.save(order);
			commonUtil.sendMailForProductOrder(saveOrder, "success");
		   }
		
	}

	@Override
	public List<OrderDetails> getOrdersByUser(Integer userId) {
		List<OrderDetails> orders = orderRepository.findByUserId(userId);
		return orders;
	}

	

}
