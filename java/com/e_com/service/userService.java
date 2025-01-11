package com.e_com.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.e_com.Entities.UserReg;

public interface userService {

	public UserReg saveUser(UserReg user);
	public UserReg getUsrByEmail(String email);
	public List<UserReg> getUsers(String role);
	public Boolean updateAccStatus(Integer id, Boolean status);
	public void increaseFailedAttmt(UserReg user);
	public void usrAccntLock(UserReg user);
	public boolean unlockAccntTimeExpired(UserReg user);
	public void resetAttmt(int usrId);
	public void updateUserResetToken(String email, String resetToken);
    public UserReg getUsrByToken (String token);
	public UserReg updateUser( UserReg user);
	public UserReg updateUserProfile(UserReg user, MultipartFile img);
	public UserReg saveAdmin(UserReg user); 
}
