package com.github.pgleska.TournamentApp.dto;

import com.github.pgleska.TournamentApp.model.ApplicationUser;

public class UserDto {

	private Long id;
	private String firstName;   
	private String lastName;    
    private String password;
    private String matchingPassword;
    private String email;
    private boolean enabled;
    private String role;

    public UserDto() { }
    
    public UserDto(Long id, String firstName, String lastName, String email, boolean enabled, String role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.enabled = enabled;
		this.role = role;
		this.password = null;
		this.matchingPassword = null;
	}
    
    public UserDto(String email, String firstName, String lastName, String password, String matchingPassword) {
    	this.email = email;
    	this.firstName = firstName;
    	this.lastName = lastName;
    	this.password = password;
    	this.matchingPassword = matchingPassword;
    }
    
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public static UserDto convert(ApplicationUser user) {
		return new UserDto(user.getId(), user.getFirstName(), user.getLastName(),
				user.getEmail(), user.isEnabled(), user.getRole());
	}
}
