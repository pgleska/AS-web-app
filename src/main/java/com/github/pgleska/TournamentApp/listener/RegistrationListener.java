package com.github.pgleska.TournamentApp.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import com.github.pgleska.TournamentApp.event.OnRegistrationCompleteEvent;
import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.service.UserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>{

	@Autowired
	private UserService userService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}
	
	private void confirmRegistration(OnRegistrationCompleteEvent event) {
        ApplicationUser user = event.getApplicationUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl 
          = event.getAppUrl() + "/user/registration/confirm?token=" + token;
        String message = "Please confirm your account"; 
        		//messages.getMessage("message.regSucc", null, event.getLocale());
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
