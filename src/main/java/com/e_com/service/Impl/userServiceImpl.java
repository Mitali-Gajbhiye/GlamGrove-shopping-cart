package com.e_com.service.Impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.e_com.Entities.UserReg;
import com.e_com.repository.userResp;
import com.e_com.service.userService;
import com.e_com.util.AppConstant;



@Service
public class userServiceImpl implements userService  {

	@Autowired
	private userResp userRep;
	
	@Autowired
	private  PasswordEncoder passwordEncoder;
	
	@Override
	public UserReg saveUser(UserReg user) {
		user.setRole("ROLE_USER");
		user.setIsEnabled(true);
		user.setAccntNonLockedd(true);
		user.setLockTime(null);
		user.setFailedAttmt(0);
		String encodePassword  = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		UserReg userSave = userRep.save(user);
		return userSave;
	}

	@Override
	public UserReg getUsrByEmail(String email) {
		
		return userRep.findByEmail(email) ;
	}
	
	@Override
	public void usrAccntLock(UserReg user) {
		user.setAccntNonLockedd(false);
		user.setLockTime(new Date());
		userRep.save(user);
		
	}

	@Override
	public boolean unlockAccntTimeExpired(UserReg user) {
		long lockTime = user.getLockTime().getTime();
		long unLockTime = lockTime + AppConstant.UNLOCK_DURATION_TIME;

		long currentTime = System.currentTimeMillis();

		if (unLockTime < currentTime) {
			user.setAccntNonLockedd(true);
			user.setFailedAttmt(0);
			user.setLockTime(null);
			userRep.save(user);
			return true;
		}

		return false;
	}

	@Override
	public void resetAttmt(int usrId) {
		
		
	}

	@Override
	public List<UserReg> getUsers(String role) {
	    
		return userRep.findByRole(role) ;
	}


	@Override
	public Boolean updateAccStatus(Integer id,Boolean status) {
		Optional<UserReg> FindbyUsr = userRep.findById(id);
		if(FindbyUsr.isPresent())
		{
			UserReg usrReg = FindbyUsr.get();
			usrReg.setIsEnabled(status);
			userRep.save(usrReg);
			return true;
		}
		return false;
	}

	@Override
	public UserReg getUsrByToken(String token) {
		return userRep.findByResetToken(token);
		
	}

	@Override
	public void updateUserResetToken(String email, String resetToken) {
		UserReg findByEmail = userRep.findByEmail(email);
		findByEmail.setResetToken(resetToken);
		userRep.save(findByEmail);
	}

	@Override
	public UserReg updateUser(UserReg user) {
		 return userRep.save(user);
	}

	@Override
	public void increaseFailedAttmt(UserReg user) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public UserReg updateUserProfile(UserReg user,  MultipartFile img) {

		UserReg dbUser = userRep.findById(user.getId()).get();

		if (!img.isEmpty()) {
			dbUser.setProfileImg(img.getOriginalFilename());
		}

		if (!ObjectUtils.isEmpty(dbUser)) {

			dbUser.setName(user.getName());
			dbUser.setMobileNo(user.getMobileNo());
			dbUser.setAddress(user.getAddress());
			dbUser.setCity(user.getCity());
			dbUser.setState(user.getState());
			dbUser.setPincode(user.getPincode());
			dbUser = userRep.save(dbUser);
		}

		try {
			if (!img.isEmpty()){
				File saveFile = new ClassPathResource("static/image").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
						+ img.getOriginalFilename());

			System.out.println(path);
				Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dbUser;
	}

	@Override
	public UserReg saveAdmin(UserReg user) {
		user.setRole("ROLE_ADMIN");
		user.setIsEnabled(true);
		user.setAccntNonLockedd(true);
		user.setFailedAttmt(0);

		String encodePassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodePassword);
		UserReg saveUser = userRep.save(user);
		return saveUser;
	}

	

}
