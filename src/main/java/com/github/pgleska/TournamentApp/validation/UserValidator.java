package com.github.pgleska.TournamentApp.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.github.pgleska.TournamentApp.dto.UserDto;
import com.github.pgleska.TournamentApp.service.UserService;

@Component
public class UserValidator implements Validator {

	private final UserService userService;
	private final Pattern pattern;
	
	public UserValidator(UserService userService) {
		this.userService = userService;
		this.pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return UserDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserDto userDto = (UserDto) target;
		
		if(userService.findById(userDto.getId()).isEmpty())
			errors.reject("id", "empty.id");
		
		Matcher matcher = pattern.matcher(userDto.getEmail());
		if(!matcher.matches())
			errors.reject("email", "invalid.email");
		
		if(userService.findByEmail(userDto.getEmail()).isPresent()) 
			errors.reject("email", "repeated.email");
		
	}

}
