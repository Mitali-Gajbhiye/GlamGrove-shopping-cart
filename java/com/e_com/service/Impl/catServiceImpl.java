package com.e_com.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.e_com.Entities.category;
import com.e_com.repository.catRespository;
import com.e_com.service.CatService;

@Service
public class catServiceImpl implements CatService {

	@Autowired
	private catRespository catRes;
	
	@Override
	public category saveCategory(category saveCat) {
		
		return catRes.save(saveCat);
	}

	@Override
	public List<category> getAllCat() {
		
		return catRes.findAll();
	}

	@Override
	public Boolean existCategory(String name){//alt+shift+s 
			
		return catRes.existsByName(name);
	
	}

	@Override
	public Boolean deleteCat(int id) {
		category cat = catRes.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(cat))
		{
			catRes.delete(cat);
			return true;
		}
		return false;
	}

	@Override
	public category getCatId(int id) {
		category catId = catRes.findById(id).orElse(null);
		return catId;
	}

	@Override
	public List<category> getAllActiveCat() {
		 List<category> categories =  catRes.findAllByisActiveTrue();
		return  categories ;
	}

	
	

}
