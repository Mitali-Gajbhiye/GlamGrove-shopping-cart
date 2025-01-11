package com.e_com.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.e_com.Entities.Categories;
import com.e_com.Entities.Category;
import com.e_com.repository.catRespository;
import com.e_com.service.CatService;

@Service
public class catServiceImpl implements CatService {

	@Autowired
	private catRespository catRes;
	
	@Override
	public Categories saveCategory(Categories saveCat) {
		
		return catRes.save(saveCat);
	}

	@Override
	public List<Categories> getAllCat() {
		
		return catRes.findAll();
	}

	@Override
	public Boolean existCategory(String name){//alt+shift+s 
			
		return catRes.existsByName(name);	
	}

	@Override
	public Boolean deleteCat(int id) {
		Categories cat = catRes.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(cat))
		{
			catRes.delete(cat);
			return true;
		}
		return false;
	}

	@Override
	public Categories getCatId(int id) {
		Categories catId = catRes.findById(id).orElse(null);
		return catId;
	}

	@Override
	public List<Categories> getAllActiveCat() {
		 List<Categories> Categories =  catRes.findAllByisActiveTrue();
		return  Categories ;
	}

	
	

}
