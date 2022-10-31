package com.github.pgleska.TournamentApp.model;

import java.io.Serializable;

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
@Table(name = "point")
public class Point implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1804028829984822034L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false)
	private ApplicationUser participant;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tournament_id", referencedColumnName = "id", nullable = false)
	private Tournament tournament;
	
	@Column(name = "license_number", unique = false, nullable = false)
	private Integer licenseNumber;
	
	@Column(name = "ranking_position", unique = false, nullable = false)
	private Integer rankingPosition;

	public Point() { }
	
	public Point(Integer licenseNumber, Integer rankingPosition) {
		this.licenseNumber = licenseNumber;
		this.rankingPosition = rankingPosition;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ApplicationUser getParticipant() {
		return participant;
	}

	public void setParticipant(ApplicationUser participant) {
		this.participant = participant;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public Integer getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(Integer licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public Integer getRankingPosition() {
		return rankingPosition;
	}

	public void setRankingPosition(Integer rankingPosition) {
		this.rankingPosition = rankingPosition;
	}
}
