package com.github.pgleska.TournamentApp.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "application_user")
public class ApplicationUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7693888071295910982L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	@Column(name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled;
	
	@Column(name = "roles")
	private String role;
	
	@OneToOne(mappedBy = "applicationUser")
	private VerificationToken verificationToken;
	
	@OneToMany(mappedBy = "organizer")
	private Set<Tournament> organizedTournaments;
	
	@OneToMany(mappedBy = "participant")
	private Set<Point> points;
	
	@OneToMany(mappedBy = "playerOne")
	private Set<Game> gamesAsPlayerOne;
	
	@OneToMany(mappedBy = "playerTwo")
	private Set<Game> gamesAsPlayerTwo;
	
	@OneToMany(mappedBy = "winner")
	private Set<Game> gamesAsWinner;
	
	@ManyToMany(targetEntity = Tournament.class, mappedBy = "members", fetch = FetchType.LAZY)
	private Set<Tournament> participatedTournaments;
	
	public ApplicationUser() { }	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String mail) {
		this.email = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	
	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public VerificationToken getVerificationToken() {
		return verificationToken;
	}

	public void setVerificationToken(VerificationToken verificationToken) {
		this.verificationToken = verificationToken;
	}

	public Set<Tournament> getOrganizedTournaments() {
		return organizedTournaments;
	}

	public void setOrganizedTournaments(Set<Tournament> organizedTournaments) {
		this.organizedTournaments = organizedTournaments;
	}

	public Set<Point> getPoints() {
		return points;
	}

	public void setPoints(Set<Point> points) {
		this.points = points;
	}

	public Set<Tournament> getParticipatedTournaments() {
		return participatedTournaments;
	}

	public void setParticipatedTournaments(Set<Tournament> participatedTournaments) {
		this.participatedTournaments = participatedTournaments;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
