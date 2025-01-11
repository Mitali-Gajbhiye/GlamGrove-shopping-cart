package com.e_com.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.e_com.Entities.UserReg;

@SuppressWarnings("serial") 
public class CustomUser implements UserDetails {

	private UserReg user;

	public CustomUser(UserReg user) {
		super();
		this.user = user;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(authority);
	}

	

	@Override
	public String getPassword() {
	
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {	
		return true;	
	}
	
	@Override
	public boolean isAccountNonLocked() {	
		return user.getAccntNonLockedd();	
	}
	@Override
	public boolean isEnabled() {	
		return user.getIsEnabled();	
	}
}
