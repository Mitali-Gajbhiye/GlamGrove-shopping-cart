package com.e_com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.e_com.Entities.UserReg;

public interface userResp extends JpaRepository<UserReg, Integer> {

	public UserReg findByEmail(String email);
	public List<UserReg>findByRole(String Role);
	public UserReg findByResetToken(String token);
	public Boolean existsByEmail(String email);
}
