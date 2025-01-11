package com.e_com.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_com.Entities.Categories;
import com.e_com.Entities.Category;

@Repository
public interface catRespository extends JpaRepository<Categories, Integer> {
	
	public Boolean existsByName(String name);

	public List<Categories> findAllByisActiveTrue();
	
	
	


}
