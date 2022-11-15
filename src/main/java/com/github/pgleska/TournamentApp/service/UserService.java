package com.github.pgleska.TournamentApp.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

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
	
	@Transactional
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
	
	@Transactional
	public void createVerificationToken(ApplicationUser user, String token) {
        VerificationToken myToken = new VerificationToken(user, token);
        tokenRepository.save(myToken);
    }
	
	@Transactional
	public VerificationToken getVerificationToken(String token) {
		return tokenRepository.findByToken(token).orElse(null);
	}
	
	@Transactional
	public void saveRegisteredUser(ApplicationUser user) {
        userRepository.save(user);
    }
	
	@Transactional
	public void deleteToken(VerificationToken token) {
		tokenRepository.delete(token);
	}
	
	@Transactional
	public void deleteUser(ApplicationUser user) {
		userRepository.delete(user);
	}
	
	@Transactional
	public void disableAccount(UserDto userDto) throws Exception {
		ApplicationUser user = userRepository.findById(Long.valueOf(userDto.getId())).orElseThrow(() -> new Exception());
		user.setEnabled(false);
	}	
	
	public List<UserDto> findAllUsers() {		
		return userRepository.findAll().stream().map(u -> UserDto.convert(u)).collect(Collectors.toList());
	}
	
	@Transactional
	public void updateUser(UserDto userDto) {
		ApplicationUser user = userRepository.findById(userDto.getId()).get();		
		if(!user.getFirstName().equals(userDto.getFirstName()))
			user.setFirstName(userDto.getFirstName());
		if(!user.getLastName().equals(userDto.getLastName()))
			user.setLastName(userDto.getLastName());
		if(!user.getEmail().equals(userDto.getEmail()))
			user.setEmail(userDto.getEmail());
		if(user.isEnabled() != userDto.isEnabled())
			user.setEnabled(userDto.isEnabled());
		if(!user.getRole().equals(userDto.getRole()))
			user.setRole(userDto.getRole());		
	}
	
	public Optional<ApplicationUser> findById(Long id) {
		return userRepository.findById(id);
	}
	
	public Optional<ApplicationUser> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public boolean checkIfAdmin(String email) {
		return userRepository.findByEmail(email).get().getRole().equals("admin");
	}
}
