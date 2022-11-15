package com.github.pgleska.TournamentApp.controller;

import java.security.Principal;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pgleska.TournamentApp.service.UserService;

@Controller
public class BaseController {
	
	private UserService userService;
	
	public BaseController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/")
	public String base() {
		return "redirect:/tournaments";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/loginfailed")
	public String loginfailed(@RequestParam("code") Integer code, Model model) {
		String message;
		if(code != null && code == 2) 
			message = "User account is locked. Try after 1 hour."; 
		else 
			message = "Incorrect login or password or user has not enabled this account.";
		model.addAttribute("message", message);
		return "lfailed";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws ServletException {
		request.logout();			
		return "redirect:/login";		
	}

	@GetMapping("/message")
	public String message(@RequestParam Integer code, Model model, Principal principal) {
		boolean admin = false;
		if(Objects.nonNull(principal))
			admin = userService.checkIfAdmin(principal.getName());
		model.addAttribute("admin", admin);
		String message = "";
		if(code == null) {
			message = "Unknown error";
		} else {
			switch(code) {
				case 1:
					message = "You must be a tournament organizer to update leaderboard.";
					break;
				case 2:
					message = "Congrats! You have joined to tournament.";
					break;
				case 3:
					message = "You've already joined to tournament.";
					break;
				case 4:
					message = "We're sorry. There are no more spots in tournament.";
					break;
				case 5:
					message = "You must choose a winner.";
					break;
				case 6:
					message = "User with such email already exists.";
					break;
				case 7:
					message = "User first name, last name, email or passwords cannot be empty. Passwords must match.";
					break;
				case 8:
					message = "Requested tournament was not found.";
					break;
				case 9:
					message = "Invalid format of tournament ID. Must be integer.";
					break;
				case 10:
					message = "Invalid format of properties.";
					break;
				case 11:
					message = "Invalid format of winner input.";
					break;
				case 12:
					message = "User could not be found.";
					break;
				case 13:
					message = "Incorrect format of license number or ranking position.";
					break;
				case 14:
					message = "Invalid format of user ID. Must be integer.";
					break;
				default:
					message = "Unknown error";
					break;
			}
		}
		model.addAttribute("message", message);
		return "message";
	}
}
