package com.github.pgleska.TournamentApp.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.github.pgleska.TournamentApp.dto.TournamentDto;
import com.github.pgleska.TournamentApp.service.TournamentService;
import com.github.pgleska.TournamentApp.service.UserService;

@Component
public class TournamentValidator implements Validator {

	private final UserService userService;
	private final TournamentService tournamentService;
	
	public TournamentValidator(UserService userService, TournamentService tournamentService) {
		this.userService = userService;
		this.tournamentService = tournamentService;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return TournamentDto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TournamentDto tournamentDto = (TournamentDto) target;
		
		if(tournamentService.findById(tournamentDto.getId()).isEmpty())
			errors.reject("id", "tournament.not.found");
		
		if(userService.findByEmail(tournamentDto.getOrganizerEmail()).isEmpty())
			errors.reject("email", "user.not.found");
	}

}
