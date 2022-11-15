package com.github.pgleska.TournamentApp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler 
	implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException exception) 
					throws IOException, ServletException {

		int code;		
		if(exception.getMessage().equals("User account is locked")) code = 2; else code = 1;
		
		response.sendRedirect(request.getContextPath() + "/loginfailed?code=" + code);				
	}
}
