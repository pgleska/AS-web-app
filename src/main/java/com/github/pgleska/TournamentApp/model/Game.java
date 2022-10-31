package com.github.pgleska.TournamentApp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "game")
public class Game {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;	
	
	@Column(name = "game_number", unique = false, nullable = false)
	private int gameNumber;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tournament_id", referencedColumnName = "id", nullable = false)
	private Tournament tournamentGame;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "first_player_id", referencedColumnName = "id", nullable = false)
	private ApplicationUser playerOne;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "second_player_id", referencedColumnName = "id", nullable = false)
	private ApplicationUser playerTwo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "winner_player_id", referencedColumnName = "id", nullable = false)
	private ApplicationUser winner;
	
	@Column(name = "old_one_id", unique = false, nullable = true)
	private Integer oldOneId;
	
	@Column(name = "old_two_id", unique = false, nullable = true)
	private Integer oldTwoId;
	
	@Transient
	private Game oldOne;
	
	@Transient
	private Game oldTwo;		
	
	public Game() { }
	
	public Game(int gameNumber, ApplicationUser one, ApplicationUser two, ApplicationUser winner) {
		this.gameNumber = gameNumber;
		this.playerOne = one;
		this.playerTwo = two;
		this.winner = winner;
		this.oldOneId = null;
		this.oldTwoId = null;
	}
	
	public Game(int gameNumber, ApplicationUser one, ApplicationUser two, ApplicationUser winner, Game oldOne, Game oldTwo) {
		this(gameNumber, one, two, winner);
		this.oldOne = oldOne;
		this.oldTwo = oldTwo;
		this.oldOneId = oldOne.getId();
		this.oldTwoId = oldTwo.getId();
	}	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public Tournament getTournamentGame() {
		return tournamentGame;
	}

	public void setTournamentGame(Tournament tournament) {
		this.tournamentGame = tournament;
	}

	public ApplicationUser getPlayerOne() {
		return playerOne;
	}

	public void setPlayerOne(ApplicationUser playerOne) {
		this.playerOne = playerOne;
	}

	public ApplicationUser getPlayerTwo() {
		return playerTwo;
	}

	public void setPlayerTwo(ApplicationUser playerTwo) {
		this.playerTwo = playerTwo;
	}

	public ApplicationUser getWinner() {
		return winner;
	}

	public void setWinner(ApplicationUser winner) {
		this.winner = winner;
	}
	
	public Integer getOldOneId() {
		return oldOneId;
	}

	public void setOldOneId(int oldOneId) {
		this.oldOneId = oldOneId;
	}

	public Integer getOldTwoId() {
		return oldTwoId;
	}

	public void setOldTwoId(int oldTwoId) {
		this.oldTwoId = oldTwoId;
	}
	
//	Elements below must annotated with @Transient

	@Transient
	public Game getOldOne() {
		return oldOne;
	}

	@Transient
	public void setOldOne(Game oldOne) {
		this.oldOne = oldOne;
	}

	@Transient
	public Game getOldTwo() {
		return oldTwo;
	}

	@Transient
	public void setOldTwo(Game oldTwo) {
		this.oldTwo = oldTwo;
	}
	
	@Transient
	public String getPlayerOneName() {
		if(this.playerOne != null)
			return 	playerOne.getFirstName() + " " + playerOne.getLastName();
		else
			return "";
	}

	@Transient
	public String getPlayerTwoName() {
		if(this.playerTwo != null)
			return playerTwo.getFirstName() + " " + playerTwo.getLastName();
		else 
			return "";
	}
	
	@Transient
	public String getWinnerName() {
		if(winner != null)
			return winner.getFirstName() + " " + winner.getLastName();
		else 
			return "";
	}

	@Transient
	public boolean isDisableSelect() {
		if(winner != null) {
			return true;
		} else if(oldOne != null && oldOne.winner == null) {
			return true;
		} else if(oldTwo != null && oldTwo.winner == null) {
			return true;
		} else {
			return false;
		}		
	}

	@Transient
	public String getPlayerOneIdentifier() {
		if(playerOne != null)
			return gameNumber + ";" + playerOne.getEmail();
		else
			return "";
	}
	
	@Transient
	public String getPlayerTwoIdentifier() {
		if(playerTwo != null)
			return gameNumber + ";" + playerTwo.getEmail();
		else
			return "";
	}
	
	@Transient
	public String getDescription() {
		String result = "Game number " + gameNumber + ": ";
		if(oldOneId != null && oldTwoId != null) {
			if(oldOne.winner != null) {
				result += oldOne.winner.getFirstName() + " " + oldOne.winner.getLastName();
			} else {
				result += "winner game no. " + oldOne.gameNumber;		
			}
			result += " vs. ";
			if(oldTwo.winner != null) {
				result += oldTwo.winner.getFirstName()  + " " + oldTwo.winner.getLastName();
			} else {
				result += "winner game no. " + oldTwo.gameNumber;		
			}
			return result;
		}
		if(playerOne != null && playerTwo != null) {
			result += playerOne.getFirstName();
			result += " ";
			result += playerOne.getLastName();
			result += " vs. ";
			result += playerTwo.getFirstName();
			result += " ";
			result += playerTwo.getLastName();
			return result;
		}
		if(playerOne != null) {
			result += playerOne.getFirstName();
			result += " ";
			result += playerOne.getLastName();
		}		
		if(playerTwo != null) {			
			result += playerTwo.getFirstName();
			result += " ";
			result += playerTwo.getLastName();
		}
		return result;
	}
}
