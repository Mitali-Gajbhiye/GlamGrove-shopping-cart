package com.e_com.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;


import com.e_com.Entities.category;

public interface catRespository extends JpaRepository<category, Integer> {
	
	public Boolean existsByName(String name);

	public List<category> findAllByisActiveTrue();
	
	
	


}
