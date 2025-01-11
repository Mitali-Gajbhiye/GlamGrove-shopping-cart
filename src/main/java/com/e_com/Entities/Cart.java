package com.e_com.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	private UserReg user;

	@ManyToOne
	private Product product;

	private Integer quantity;
	
	@Transient
	private Double totalPrice;
	
	public Cart() {
		super();
		
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", user=" + user + ", product=" + product + ", quantity=" + quantity + ", totalPrice="
				+ totalPrice + ", totalOrderPrice=" + totalOrderPrice + "]";
	}

	public Cart(Integer id, UserReg user, Product product, Integer quantity, Double totalPrice,
			Double totalOrderPrice) {
		super();
		this.id = id;
		this.user = user;
		this.product = product;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.totalOrderPrice = totalOrderPrice;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserReg getUser() {
		return user;
	}

	public void setUser(UserReg user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Double getTotalOrderPrice() {
		return totalOrderPrice;
	}

	public void setTotalOrderPrice(Double totalOrderPrice) {
		this.totalOrderPrice = totalOrderPrice;
	}

	@Transient
	private Double totalOrderPrice;
}
