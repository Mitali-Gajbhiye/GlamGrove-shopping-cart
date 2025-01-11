package com.e_com.Entities;

import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class UserReg {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String mobileNo;
	private String email;
	private String address;
	private String city;
	private String pincode;
	private String state;
	private String password;
	private String profileImg;
	private String cpassword; 
	private String role;
	private Boolean isEnabled;
	private Boolean accntNonLockedd;
	private Integer failedAttmt;
	private Date lockTime;
	private String resetToken;
	
	
	
	
	
	
	
}
