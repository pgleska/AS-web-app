package com.github.pgleska.TournamentApp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.pgleska.TournamentApp.dto.UserDto;
import com.github.pgleska.TournamentApp.exception.UserAlreadyExistsException;
import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.VerificationToken;
import com.github.pgleska.TournamentApp.repository.UserRepository;
import com.github.pgleska.TournamentApp.repository.VerificationTokenRepository;

@Service
public class UserService {
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;	
	private final VerificationTokenRepository tokenRepository; 
	private final UserRepository userRepository;
	
	public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, VerificationTokenRepository tokenRepository) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
	}
	
	public ApplicationUser registerNewUserAccount(UserDto userDto) throws UserAlreadyExistsException {
		if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistsException("There is an account with that email address: " + userDto.getEmail());
        }
		
		ApplicationUser user = new ApplicationUser();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setEnabled(false);
        user.setRole("user");

        return userRepository.save(user);
	}
	
	private boolean emailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
	
	public void createVerificationToken(ApplicationUser user, String token) {
        VerificationToken myToken = new VerificationToken(user, token);
        tokenRepository.save(myToken);
    }
	
	public VerificationToken getVerificationToken(String token) {
		return tokenRepository.findByToken(token).orElse(null);
	}
	
	public void saveRegisteredUser(ApplicationUser user) {
        userRepository.save(user);
    }
	
	public void deleteToken(VerificationToken token) {
		tokenRepository.delete(token);
	}
	
	public void deleteUser(ApplicationUser user) {
		userRepository.delete(user);
	}
	
	public void disableAccount(UserDto userDto) throws Exception {
		ApplicationUser user = userRepository.findById(Long.valueOf(userDto.getId())).orElseThrow(() -> new Exception());
		user.setEnabled(false);
	}
}
