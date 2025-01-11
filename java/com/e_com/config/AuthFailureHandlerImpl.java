package com.e_com.config;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.e_com.Entities.UserReg;
import com.e_com.repository.userResp;
import com.e_com.service.userService;
import com.e_com.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private userResp userRepository;

	@Autowired
	private userService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String email = request.getParameter("username");

		UserReg userDtls = userRepository.findByEmail(email);

		if (userDtls != null) {

			if (userDtls.getIsEnabled()) {

				if (userDtls.getAccntNonLockedd()) {

					if (userDtls.getFailedAttmt() <= AppConstant.ATTEMPT_TIME) {
						userService.increaseFailedAttmt(userDtls);
					} else {
						userService.usrAccntLock(userDtls);
						exception = new LockedException("Your account is locked !! failed attempt 3");
					}
				} else {

					if (userService.unlockAccntTimeExpired(userDtls)) {
						exception = new LockedException("Your account is unlocked !! Please try to login");
					} else {
						exception = new LockedException("your account is Locked !! Please try after sometime");
					}
				}

			} else {
				exception = new LockedException("your account is inactive");
			}
		} else {
			exception = new LockedException("Email & password invalid");
		}

		super.setDefaultFailureUrl("/slogin?error");
		super.onAuthenticationFailure(request, response, exception);
	}

}
