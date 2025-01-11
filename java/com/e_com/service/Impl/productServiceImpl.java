package com.e_com.service.Impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.e_com.Entities.Product;
import com.e_com.repository.productRespository;
import com.e_com.service.productService;

@Service
public class productServiceImpl implements productService{

	@Autowired
	private productRespository productResp;
	
	@Override
	public Product saveProduct(Product product) {
		
		return productResp.save(product);
	}

	@Override
	public List<Product> getAllFlies() {
		
		return productResp.findAll();
	}

	@Override
	public boolean deleteFlies(Integer id) {
		Product product = productResp.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(product))
		{
			productResp.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getFliesById(Integer id) {
		Product product = productResp.findById(id).orElse(null);
		return product;
	}

	@Override
	public Product updateProduct(Product pro, MultipartFile image) {
		Product updatePro = getFliesById(pro.getId());
		String imageName = image.isEmpty() ? updatePro.getImage() : image.getOriginalFilename();
		
		updatePro.setTitle(pro.getTitle());
		updatePro.setDescription(pro.getDescription());
		updatePro.setCategory(pro.getCategory());
		updatePro.setPrice(pro.getPrice());
		updatePro.setStock(pro.getStock());
		updatePro.setImage(imageName);
		updatePro.setDiscount(pro.getDiscount());
		updatePro.setIsActive(pro.getIsActive());
		
		Double discount = pro.getPrice() * (pro.getDiscount() /100.0);
		Double discountPrice = pro.getPrice() - discount;
		updatePro.setDiscountPrice(discountPrice);
		
		Product updateProduct = productResp.save(updatePro);
		if(!ObjectUtils.isEmpty(updateProduct))
		{
			
			if(!image.isEmpty())
			{
				try {
				File saveFile = new ClassPathResource("static/image").getFile();
				
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "productImg"
				+ File.separator + image.getOriginalFilename());
				
				Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}catch(Exception e)  { e.printStackTrace();}
			}
			
			
			return pro;
		}
		
		return null;
	}

	@Override
	public List<Product> getAllActiveFlies(String category) {
		List<Product> flies = productResp.findByIsActiveTrue();
		if (ObjectUtils.isEmpty(category)) {
			flies = productResp.findByIsActiveTrue();
		} else {
			flies = productResp.findByCategory(category);
		}
		return flies;
	}

	

	
	

}
