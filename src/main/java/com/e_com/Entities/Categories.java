package com.e_com.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Categories {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String imageName;
	private Boolean isActive;
	/*@Override
	public String toString() {
		return "Categories [id=" + id + ", name=" + name + ", imageName=" + imageName + ", isActive=" + isActive + "]";
	}
	public Categories() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Categories(int id, String name, String imageName, boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		this.imageName = imageName;
		this.isActive = isActive;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}*/

}
