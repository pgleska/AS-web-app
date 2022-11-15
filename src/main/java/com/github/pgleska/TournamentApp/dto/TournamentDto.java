package com.github.pgleska.TournamentApp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pgleska.TournamentApp.model.Tournament;

public class TournamentDto {

	private Integer id;
	
	@NotBlank
	private String name;
	
	private String discipline;
	
	@NotNull
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String startingDate;
	
	@Min(-90)
	@Max(90)
	private Double latitude;
	
	@Min(-180)
	@Max(180)
	private Double longitude;
		
	private Integer participants;
	
	@Min(2)
	@Max(1024)
	@NotNull
	private Integer maxNumberOfParticipants;
	
	private String organizerEmail;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDiscipline() {
		return discipline;
	}
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	public String getStartingDate() {
		return startingDate;
	}
	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Integer getParticipants() {
		return participants;
	}
	public void setParticipants(Integer participants) {
		this.participants = participants;
	}
	public Integer getMaxNumberOfParticipants() {
		return maxNumberOfParticipants;
	}
	public void setMaxNumberOfParticipants(Integer maxNumberOfParticipants) {
		this.maxNumberOfParticipants = maxNumberOfParticipants;
	}
	public String getOrganizerEmail() {
		return organizerEmail;
	}
	public void setOrganizerEmail(String organizerEmail) {
		this.organizerEmail = organizerEmail;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public static TournamentDto convert(Tournament tournament) {
		TournamentDto tournamentDto = new TournamentDto();
		tournamentDto.setId(tournament.getId());
		tournamentDto.setName(tournament.getName());
		tournamentDto.setDiscipline(tournament.getDiscipline());
		tournamentDto.setStartingDate(tournament.getStartingDate().toString());
		tournamentDto.setParticipants(tournament.getParticipants());
		tournamentDto.setMaxNumberOfParticipants(tournament.getMaxNumberOfParticipants());
		tournamentDto.setOrganizerEmail(tournament.getOrganizer().getEmail());
		return tournamentDto;
	}
}
