package com.github.pgleska.TournamentApp.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BaseController {
	
	@GetMapping("/")
	public String base() {
		return "redirect:/tournaments";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/loginfailed")
	public String loginfailed() {
		return "lfailed";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws ServletException {
		request.logout();			
		return "redirect:/login";		
	}

	@GetMapping("/message")
	public String message(@RequestParam Integer code, Model model) {
		String message = "";
		if(code == null) {
			message = "Unknown error";
		} else {
			switch(code) {
				case 1:
					message = "You must be a tournament organizer to update leaderboard.";
					break;
				case 2:
					message = "Congrats! You joined to tournament.";
					break;
				case 3:
					message = "You've already joined to tournament.";
					break;
				case 4:
					message = "We're sorry. There's no more spots in tournament.";
					break;
				case 5:
					message = "You must choose winner.";
					break;
				case 6:
					message = "User with such email already exists.";
					break;
				case 7:
					message = "User first name, last name, email or passwords cannot be empty. Passwords must match.";
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
