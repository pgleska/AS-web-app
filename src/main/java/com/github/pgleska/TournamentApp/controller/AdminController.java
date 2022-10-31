package com.github.pgleska.TournamentApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pgleska.TournamentApp.dto.UserDto;
import com.github.pgleska.TournamentApp.service.UserService;

@Controller
@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/pane")
	public String getAdminPane() {
		return "admin_pane";
	}

	//delete user - not perma but disable account
	@PatchMapping(value = "/pane/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String disableAccount(UserDto user) {
		
		try {
			userService.disableAccount(user);
		} catch(Exception E) {
			
		}
		
		return "redirect:/message=2";
	}
	
	/*TODO: endpoints
	- change organizer of tournament
	- update user
	*/
}
