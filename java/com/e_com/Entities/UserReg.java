package com.e_com.Entities;

import java.util.Date;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
public class UserReg {

	public UserReg() {
		super();
		
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return "UserReg [id=" + id + ", name=" + name + ", mobileNo=" + mobileNo + ", email=" + email + ", address="
				+ address + ", city=" + city + ", pincode=" + pincode + ", state=" + state + ", password=" + password
				+ ", profileImg=" + profileImg + ", cpassword=" + cpassword + ", role=" + role + ", isEnabled="
				+ isEnabled + ", accntNonLockedd=" + accntNonLockedd + ", failedAttmt=" + failedAttmt + ", lockTime="
				+ lockTime + "]";
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getCpassword() {
		return cpassword;
	}
	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}
	public boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
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
	
	public String getResetToken() {
		return resetToken;
	}
	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
	public UserReg(Integer id, String name, String mobileNo, String email, String address, String city, String pincode,
			String state, String password, String profileImg, String cpassword, String role, Boolean isEnabled,
			Boolean accntNonLockedd, Integer failedAttmt, Date lockTime, String resetToken) {
		super();
		this.id = id;
		this.name = name;
		this.mobileNo = mobileNo;
		this.email = email;
		this.address = address;
		this.city = city;
		this.pincode = pincode;
		this.state = state;
		this.password = password;
		this.profileImg = profileImg;
		this.cpassword = cpassword;
		this.role = role;
		this.isEnabled = isEnabled;
		this.accntNonLockedd = accntNonLockedd;
		this.failedAttmt = failedAttmt;
		this.lockTime = lockTime;
		this.resetToken = resetToken;
	}
	public Boolean getAccntNonLockedd() {
		return accntNonLockedd;
	}
	public void setAccntNonLockedd(Boolean accntNonLockedd) {
		this.accntNonLockedd = accntNonLockedd;
	}
	public Integer getFailedAttmt() {
		return failedAttmt;
	}
	public void setFailedAttmt(Integer failedAttmt) {
		this.failedAttmt = failedAttmt;
	}
	public Date getLockTime() {
		return lockTime;
	}
	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}
	
	
	
	
	
}
