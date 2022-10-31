package com.github.pgleska.TournamentApp.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.github.pgleska.TournamentApp.dto.UserDto;
import com.github.pgleska.TournamentApp.event.OnRegistrationCompleteEvent;
import com.github.pgleska.TournamentApp.event.OnResetPasswordEvent;
import com.github.pgleska.TournamentApp.exception.UserAlreadyExistsException;
import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.VerificationToken;
import com.github.pgleska.TournamentApp.repository.UserRepository;
import com.github.pgleska.TournamentApp.service.UserService;

@Controller
@RequestMapping("/user")
public class ApplicationUserController {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final ApplicationEventPublisher eventPublisher;	
	private final UserService userService;	
	private final UserRepository userRepository;
	
	public ApplicationUserController(
				BCryptPasswordEncoder bCryptPasswordEncoder,
				UserService userService,
				ApplicationEventPublisher eventPublisher,
				UserRepository userRepository) {
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userService = userService;
		this.eventPublisher = eventPublisher;
		this.userRepository = userRepository;
	}		

	@GetMapping("/registration")
	public String showRegistrationForm(Model model) {
	    UserDto userDto = new UserDto();
	    model.addAttribute("user", userDto);
	    return "registration";
	}
	
	@GetMapping("/registration/expired")
	public String registrationExpired(Model model) {	    
	    return "registration_expired";
	}
	
	@GetMapping("/registration/missing")
	public String missing(Model model) {	    
	    return "registration_missing";
	}
	
	@GetMapping("/registration/error")
	public String errorRegistration(Model model, @RequestParam(value = "code", required = false) Integer code) {	
		String error = "";
		switch(code) {
			case 1:
				error = "First name cannot be empty or blank.";
				break;
			case 2:
				error = "Last name cannot be empty or blank.";
				break;
			case 3:
				error = "Email cannot be empty or blank.";
				break;
			case 4:
				error = "Password cannot be empty or blank.";
				break;
			case 5:
				error = "Passwords must much each others.";
				break;
			case 6:
				error = "User with such email already exists.";
				break;
			default:
				error = "Unknown error occured.";
				break;
		}
		model.addAttribute("error", error);
	    return "registration_error";
	}
	
	@PostMapping("/registration")
	public String registerUserAccount(
			@ModelAttribute("user") UserDto userDto, HttpServletRequest request,
			BindingResult errors, ModelAndView mav) {	
		if(userDto.getFirstName().isEmpty() || userDto.getFirstName().isBlank())
			return "redirect:/user/registration/error?code=1";
		if(userDto.getLastName().isEmpty() || userDto.getLastName().isBlank())
			return "redirect:/user/registration/error?code=2";
		if(userDto.getEmail().isEmpty() || userDto.getEmail().isBlank())
			return "redirect:/user/registration/error?code=3";
		if(userDto.getPassword().isEmpty() || userDto.getPassword().isBlank())
			return "redirect:/user/registration/error?code=4";
		if(!userDto.getMatchingPassword().equals(userDto.getPassword()))
			return "redirect:/user/registration/error?code=5";
		
	    try {
	        ApplicationUser registered = userService.registerNewUserAccount(userDto);
	        
	        String appUrl = request.getContextPath();
	        String link = createConfirmationLink(registered, appUrl);
//	        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
	        return "redirect:/user/registration/success?status=new&link=" + link;
	    } catch (UserAlreadyExistsException uaeEx) {
	        return "redirect:/user/registration/error?code=6";
	    }	    
	}
	
	//TODO: when email confirmation is disabled
	private String createConfirmationLink(ApplicationUser user, String url) {
		String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String confirmationUrl = url + "/user/registration/confirm?token=" + token;
		return confirmationUrl;
	}
	
	@GetMapping("/registration/confirm")
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {	 	   	    
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {	        
	        return "redirect:/user/registration/expired";
	    }
	    
	    ApplicationUser user = verificationToken.getApplicationUser();
	    LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
	    if (ChronoUnit.MINUTES.between(now, verificationToken.getExpiryDate()) <= 0) {
	    	userService.deleteToken(verificationToken);
	    	return "redirect:/user/registration/expired";
	    } 
	    
	    user.setEnabled(true); 
	    user.setVerificationToken(null);
	    userService.saveRegisteredUser(user);
	    userService.deleteToken(verificationToken);
	    return "redirect:/user/registration/success?status=confirmed"; 
	}
	
	@GetMapping("/registration/success")
	public String registrationSuccess(Model model, @RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "link", required = false) String link) {
		boolean result = status.equals("confirmed")  ? true : false;
		model.addAttribute("status", result);
		if(!result && link != null) model.addAttribute("link", link);
		return "registration_success";
	}
	
	@GetMapping("/registration/forget")
	public String forgetPassword(Model model) {
		return "forget_password";
	}
	
	@PostMapping("/registration/forget")
	public String resetPassword(Model model, @RequestParam Map<String, String> body, HttpServletRequest request) {
		String email = "";
		if(body.containsKey("email"))
			email = body.get("email");
		
		Optional<ApplicationUser> user = userRepository.findByEmail(email);
		if(user.isEmpty())
			return "redirect:/user/registration/missing";
		
		String appUrl = request.getContextPath();
        eventPublisher.publishEvent(new OnResetPasswordEvent(user.get(), appUrl));
		return "reset_success";
	}
	
	@GetMapping("/registration/forget/new")
	public String forgetPassword(Model model, @RequestParam("token") String token) {
		VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {	        
	        return "redirect:/user/registration/expired";
	    }
	    	    
	    LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
	    if (ChronoUnit.MINUTES.between(now, verificationToken.getExpiryDate()) <= 0) {
	    	userService.deleteToken(verificationToken);
	    	return "redirect:/user/registration/expired";
	    } 
	    
	    model.addAttribute("token", token);
	    return "reset_password";
	}
	
	@Transactional
	@PostMapping("/registration/forget/new")
	public String newPassword(Model model, @RequestParam Map<String,String> body) {
		String token = body.get("token");
		VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {	        
	        return "redirect:/user/registration/expired";
	    }
	    	    
	    LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
	    if (ChronoUnit.MINUTES.between(now, verificationToken.getExpiryDate()) <= 0) {
	    	userService.deleteToken(verificationToken);
	    	return "redirect:/user/registration/expired";
	    } 
	    
	    ApplicationUser user = verificationToken.getApplicationUser();
	    user.setPassword(bCryptPasswordEncoder.encode(body.get("password")));
	    user.setVerificationToken(null);
	    userRepository.save(user);	    
	    userService.deleteToken(verificationToken);
	    return "redirect:/login";
	}
}
