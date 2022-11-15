package com.github.pgleska.TournamentApp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "login_attempt")
public class LoginAttempt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, updatable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,updatable = false)
	private ApplicationUser user;
	
	@Column(name = "status", nullable = false, updatable = false)
	private Integer status;
	
	@Column(name = "attempt_time", nullable = false, updatable = false)
	private LocalDateTime attemptTime;
	
	public LoginAttempt() { }
	
	public LoginAttempt(ApplicationUser user, Integer status) { 
		this.user = user;
		this.status = status;
		attemptTime = LocalDateTime.now();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public ApplicationUser getUser() {
		return user;
	}
	
	public void setUser(ApplicationUser user) {
		this.user = user;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public LocalDateTime getAttemptTime() {
		return attemptTime;
	}
	
	public void setAttemptTime(LocalDateTime attemptTime) {
		this.attemptTime = attemptTime;
	}
}
