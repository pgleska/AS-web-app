package com.github.pgleska.TournamentApp.model;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "verification_token")
public class VerificationToken implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5795796284088553969L;

	private static final int EXPIRATION = 60 * 60 * 24;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "token", unique = true, nullable = false)
	private String token;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private ApplicationUser applicationUser;
	
	@Column(name = "expiry_date", unique = true, nullable = false)
	private LocalDateTime expiryDate;
	
	public VerificationToken() { }
	
	public VerificationToken(ApplicationUser user, String token) {
		this.applicationUser = user;
		this.token = token;
		this.expiryDate = calculateExpiryDate();
	}
	
	private LocalDateTime calculateExpiryDate() {
		Instant timestamp = Instant.now();
		Instant newTimesamtp = timestamp.plusSeconds(EXPIRATION);
		return LocalDateTime.ofInstant(newTimesamtp, ZoneOffset.UTC);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ApplicationUser getApplicationUser() {
		return applicationUser;
	}

	public void setApplicationUser(ApplicationUser applicationUser) {
		this.applicationUser = applicationUser;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
}
