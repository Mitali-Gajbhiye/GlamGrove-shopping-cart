package com.e_com.service;

import java.util.List;

import com.e_com.Entities.category;

public interface CatService {

	 public category saveCategory(category saveCat);
	 
	 public Boolean existCategory(String name);
	 
	 public List<category> getAllCat();
	 
	 public Boolean deleteCat(int id);
	 
	 public category getCatId(int id);
	 
	 public List<category> getAllActiveCat();
	 
	
}
