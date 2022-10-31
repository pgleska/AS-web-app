package com.github.pgleska.TournamentApp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.github.pgleska.TournamentApp.dto.TournamentDto;

@Entity
@Table(name = "tournament")
public class Tournament implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6008321111955431085L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@Column(name = "discipline", unique = false, nullable = false)
	private String discipline;
	
	@Column(name = "starting_date", unique = false, nullable = false)
	private LocalDate startingDate;
	
	@Column(name = "latitude", unique = false, nullable = false)
	private Double latitude;
	
	@Column(name = "longitude", unique = false, nullable = false)
	private Double longitude;
	
	@Column(name = "participants", unique = false, nullable = false)
	private Integer participants;
	
	@Column(name = "max_number_of_participants", unique = false, nullable = false)
	private Integer maxNumberOfParticipants;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organizer_id", referencedColumnName = "id", nullable = false)
	private ApplicationUser organizer;
	
	@OneToMany(mappedBy = "tournament")
	private Set<Point> points;
	
	@OneToMany(mappedBy = "tournamentGame")
	private List<Game> games;
	
	@ManyToMany(targetEntity = ApplicationUser.class, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tournament_participant",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
        )
	private Set<ApplicationUser> members;
	
	@Column(name = "ladder_generated", nullable = false)
	private Boolean ladderGenerated;
	
	public Tournament() { }
	
	public Tournament(Integer id, String name, String discipline, LocalDate startingDate,
			Double latitude, Double longitude, Integer participants, Integer maxNumberOfparticipants) {
		this.id = id;
		this.name = name;
		this.discipline = discipline;
		this.startingDate = startingDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.participants = participants;
		this.maxNumberOfParticipants = maxNumberOfparticipants;
		this.ladderGenerated = false;
	}
	
	public Tournament(Integer id, String name, String discipline, LocalDate startingDate,
			Double latitude, Double longitude, Integer participants, Integer maxNumberOfparticipants,
			ApplicationUser organizer) {
		this(id, name, discipline, startingDate, latitude, longitude, participants, maxNumberOfparticipants);
		this.organizer = organizer;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
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
	
	public LocalDate getStartingDate() {
		return startingDate;
	}
	
	public void setStartingDate(LocalDate startDate) {
		this.startingDate = startDate;
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
	
	public ApplicationUser getOrganizer() {
		return organizer;
	}
	
	public void setOrganizer(ApplicationUser organizer) {
		this.organizer = organizer;
	}
		
	public Set<Point> getPoints() {
		return points;
	}

	public void setPoints(Set<Point> points) {
		this.points = points;
	}

	public Set<ApplicationUser> getMembers() {
		return members;
	}

	public void setMembers(Set<ApplicationUser> members) {
		this.members = members;
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}
	
	public Boolean getLadderGenerated() {
		return ladderGenerated;
	}

	public void setLadderGenerated(Boolean ladderGenerated) {
		this.ladderGenerated = ladderGenerated;
	}

	public static class Builder {
		public static Tournament build(TournamentDto dto, ApplicationUser requester) {
			Tournament tournament = new Tournament();
			tournament.setName(dto.getName());
			tournament.setDiscipline("SoloQ");
			tournament.setStartingDate(LocalDate.parse(dto.getStartingDate()));
			tournament.setMaxNumberOfParticipants(dto.getMaxNumberOfParticipants());
			tournament.setLatitude(dto.getLatitude());
			tournament.setLongitude(dto.getLongitude());
			tournament.setParticipants(0);
			tournament.setOrganizer(requester);
			tournament.setLadderGenerated(false);
			return tournament;
		}
	}
}
