package com.e_com.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.e_com.Entities.Product;

public interface productService {

	public Product saveProduct(Product product);
	public List<Product> getAllFlies();
	public boolean deleteFlies(Integer id);
	public Product getFliesById(Integer id);
	public Product updateProduct(Product pro, MultipartFile image);
	public List<Product> getAllActiveFlies(String category);
}
