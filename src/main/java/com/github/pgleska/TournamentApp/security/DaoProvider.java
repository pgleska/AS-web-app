package com.github.pgleska.TournamentApp.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.LoginAttempt;
import com.github.pgleska.TournamentApp.repository.LoginAttemptRepository;
import com.github.pgleska.TournamentApp.repository.UserRepository;

public class DaoProvider extends DaoAuthenticationProvider {
	
    private UserRepository userRepository;
	private LoginAttemptRepository loginAttemptRepository;
	
	@Override
	@SuppressWarnings("deprecation")
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			this.logger.debug("Failed to authenticate since no credentials provided");
			throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
		String presentedPassword = authentication.getCredentials().toString();
		if (!this.getPasswordEncoder().matches(presentedPassword, userDetails.getPassword())) {
			this.logger.debug("Failed to authenticate since password does not match stored value");
			ApplicationUser user = userRepository.findByEmail(userDetails.getUsername()).get();
			LoginAttempt loginAttempt = new LoginAttempt(user, 0);
			loginAttemptRepository.save(loginAttempt);
			throw new BadCredentialsException(this.messages
					.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
		}
	}
	
	public void setLoginAttemptRepository(LoginAttemptRepository loginAttemptRepository) {
		this.loginAttemptRepository = loginAttemptRepository;
	}
	
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
