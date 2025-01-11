package com.e_com.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.e_com.Entities.UserReg;
import com.e_com.repository.userResp;

@Service
public class userDetailServiceImpl implements UserDetailsService {

	@Autowired
	private userResp usrRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserReg usr = usrRepo.findByEmail(username);
		if(usr == null)
		{
			throw new UsernameNotFoundException("user not found");
		}		
		return new CustomUser(usr);
	}

}
