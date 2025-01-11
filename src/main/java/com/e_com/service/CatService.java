package com.e_com.service;

import java.util.List;

import com.e_com.Entities.Categories;

public interface CatService {

	Categories saveCategory(Categories saveCat);
	 
	  Boolean existCategory(String name);
	 
	  List<Categories> getAllCat();
	 
	  Boolean deleteCat(int id);
	 
	  Categories getCatId(int id);
	 
	  List<Categories> getAllActiveCat();
	 
	
}
