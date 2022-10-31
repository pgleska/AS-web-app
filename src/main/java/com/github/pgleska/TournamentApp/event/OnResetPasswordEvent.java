package com.github.pgleska.TournamentApp.event;

import org.springframework.context.ApplicationEvent;

import com.github.pgleska.TournamentApp.model.ApplicationUser;

public class OnResetPasswordEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8880960711880930088L;
	
	private String appUrl;
	private ApplicationUser applicationUser;
	
	public OnResetPasswordEvent(ApplicationUser applicationUser, String appUrl) {
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
