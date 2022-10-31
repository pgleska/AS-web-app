package com.github.pgleska.TournamentApp.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.github.pgleska.TournamentApp.event.OnResetPasswordEvent;
import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.service.UserService;

@Component
public class ResetListener implements ApplicationListener<OnResetPasswordEvent> {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(OnResetPasswordEvent event) {
		this.resetPassword(event);
	}
	
	private void resetPassword(OnResetPasswordEvent event) {
        ApplicationUser user = event.getApplicationUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        
        String recipientAddress = user.getEmail();
        String subject = "Reset Password";
        String confirmationUrl 
          = event.getAppUrl() + "/user/registration/forget/new?token=" + token;
        String message = "Reset your password"; 
        
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("janusz3564@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
