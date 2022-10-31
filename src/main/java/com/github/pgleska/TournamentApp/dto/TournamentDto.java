package com.github.pgleska.TournamentApp.dto;

public class TournamentDto {
	private String name;	
	private String discipline;
	private String startingDate;
	private Double latitude;
	private Double longitude;
	private Integer participants;
	private Integer maxNumberOfParticipants;
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
}
