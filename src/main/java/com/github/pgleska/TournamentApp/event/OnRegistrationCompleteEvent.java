package com.github.pgleska.TournamentApp.event;

import org.springframework.context.ApplicationEvent;

import com.github.pgleska.TournamentApp.model.ApplicationUser;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1828071382479448585L;
	
	private String appUrl;
	private ApplicationUser applicationUser;
	
	public OnRegistrationCompleteEvent(ApplicationUser applicationUser, String appUrl) {
		super(applicationUser);
		
		this.applicationUser = applicationUser;
		this.appUrl = appUrl;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public ApplicationUser getApplicationUser() {
		return applicationUser;
	}

	public void setApplicationUser(ApplicationUser applicationUser) {
		this.applicationUser = applicationUser;
	}
}
