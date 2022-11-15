package com.github.pgleska.TournamentApp.controller;

import java.security.Principal;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pgleska.TournamentApp.dto.TournamentDto;
import com.github.pgleska.TournamentApp.dto.UserDto;
import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.Tournament;
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
	
	@GetMapping(value = "/pane/{ids}")
	public String getUser(Principal principal, Model model, @PathVariable("ids") String ids) {		
		Long id = 0l;
		try {
			id = Long.valueOf(ids);
		} catch (NumberFormatException e) {
			return "redirect:/message?code=14";
		}
		ApplicationUser applicationUser = userService.findById(id).orElse(null);
		if(Objects.isNull(applicationUser))
			return "redirect:/message?code=12";
		UserDto userDto = UserDto.convert(applicationUser);	
		model.addAttribute("user", userDto);
		return "user_pane";
	}

	//delete user - not perma but disable account
	@PostMapping(value = "/pane/users")
	public String updateUserAsAdmin(@ModelAttribute("user") UserDto userDto) {
		
		DataBinder dataBinder = new DataBinder(userDto);
		dataBinder.setValidator(userValidator);
		BindingResult bindingResult = dataBinder.getBindingResult();
		userValidator.validate(userDto, bindingResult);
		
		if(bindingResult.hasErrors()) {
			return "redirect:/admin/pane";
		}
		
		userService.updateUser(userDto);
		return "redirect:/admin/pane";
	}	
}
