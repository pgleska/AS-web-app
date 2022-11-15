package com.github.pgleska.TournamentApp.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pgleska.TournamentApp.dto.TournamentDto;
import com.github.pgleska.TournamentApp.dto.UserDto;
import com.github.pgleska.TournamentApp.service.TournamentService;
import com.github.pgleska.TournamentApp.service.UserService;
import com.github.pgleska.TournamentApp.validation.TournamentValidator;
import com.github.pgleska.TournamentApp.validation.UserValidator;

@Controller
@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/admin")
public class AdminController {
	
	private final UserService userService;
	private final TournamentService tournamentService;
	private final UserValidator userValidator;
	private final TournamentValidator tournamentValidator;
	
	public AdminController(UserService userService, TournamentService tournamentService,
			UserValidator userValidator, TournamentValidator tournamentValidator) {
		this.userService = userService;
		this.tournamentService = tournamentService;
		this.userValidator = userValidator;
		this.tournamentValidator = tournamentValidator;
	}
	
	@GetMapping(value = "/pane")
	public String getAdminPane(Principal principal, Model model) {		
		model.addAttribute("users", userService.findAllUsers());
		model.addAttribute("tournaments", tournamentService.findAllTournaments());
		return "admin_pane";
	}			

	//delete user - not perma but disable account
	@PostMapping(value = "/pane/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUserAsAdmin(@RequestBody UserDto userDto) {
		
		DataBinder dataBinder = new DataBinder(userDto);
		dataBinder.setValidator(userValidator);
		BindingResult bindingResult = dataBinder.getBindingResult();
		userValidator.validate(userDto, bindingResult);
		
		if(bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		userService.updateUser(userDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping(value = "/pane/tournaments", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTournamentsAdminPane(@RequestBody TournamentDto tournamentDto) {
		DataBinder dataBinder = new DataBinder(tournamentDto);
		dataBinder.setValidator(tournamentValidator);
		BindingResult bindingResult = dataBinder.getBindingResult();
		tournamentValidator.validate(tournamentDto, bindingResult);
		
		if(bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		tournamentService.updateTournament(tournamentDto);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
