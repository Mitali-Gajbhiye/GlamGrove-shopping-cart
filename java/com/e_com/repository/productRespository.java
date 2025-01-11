package com.e_com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_com.Entities.Product;

public interface productRespository extends JpaRepository<Product, Integer>{

	List<Product> findByIsActiveTrue();
	List<Product> findByCategory(String category);

	
}
