package com.github.pgleska.TournamentApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pgleska.TournamentApp.dto.PointDto;
import com.github.pgleska.TournamentApp.dto.TournamentDto;
import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.Game;
import com.github.pgleska.TournamentApp.model.Point;
import com.github.pgleska.TournamentApp.model.Tournament;
import com.github.pgleska.TournamentApp.repository.GameRepository;
import com.github.pgleska.TournamentApp.repository.PointRepository;
import com.github.pgleska.TournamentApp.repository.TournamentRepository;
import com.github.pgleska.TournamentApp.repository.UserRepository;
import com.github.pgleska.TournamentApp.util.PointComparator;

@Service
public class TournamentService {
	private final GameRepository gameRepository;
	private final PointRepository pointRepository;
	private final TournamentRepository tournamentRepository;
	private final UserRepository userRepository;
	
	public TournamentService(GameRepository gameRepository, PointRepository pointRepository, 
			TournamentRepository tournamentRepository, UserRepository userRepository) {
		this.gameRepository = gameRepository;
		this.pointRepository = pointRepository;
		this.tournamentRepository = tournamentRepository;
		this.userRepository = userRepository;
	}
	
	public Tournament createTournament(TournamentDto dto, ApplicationUser reuqester) {
		Tournament tournament = Tournament.Builder.build(dto, reuqester);
		tournamentRepository.save(tournament);
		return tournament;
	}
	
	@Transactional
	public String joinToTournament(Integer id, ApplicationUser requester, PointDto oldPoint) {
		Tournament tournament = tournamentRepository.findById(id).orElse(null);
		
		if(Objects.isNull(tournament)) return "missing";
		
		if(tournament.getParticipants() < tournament.getMaxNumberOfParticipants()) {
			Set<ApplicationUser> members = tournament.getMembers();
			if(members.add(requester)) {
				tournament.setParticipants(tournament.getParticipants() + 1);
				tournament.setMembers(members);
				Point point = new Point(oldPoint.getLicenseNumber(), oldPoint.getRankingPosition());
				point.setTournament(tournament);
				point.setParticipant(requester);
				pointRepository.save(point);
				return "success";
			}
			else 
				return "present";
		} else {
			return "full";
		}
	}
	
	@Transactional
	public List<Game> createLadder(Tournament tournament) {
		List<Point> players = new ArrayList<>(tournament.getPoints());		
		players.sort(new PointComparator());
		
		int size = players.size();
		int maxDepth = (int) Math.ceil(Math.log(size) / Math.log(2));
		int currDepth = 0;		
		int numberOfGamesInRound, currOldGameIdx = 0;		
		int gameNumber = 1;
		Game g;
		List<Game> games = new ArrayList<>();
		
		for(currDepth = 0; currDepth < maxDepth; currDepth++) {
			numberOfGamesInRound = (int) Math.pow(2, maxDepth - 1 - currDepth);
			if(currDepth == 0) {
				for(int i = 0; i < numberOfGamesInRound; i++) {					
					ApplicationUser hp = getHigherPlayer(players, numberOfGamesInRound - 1 - i);
					ApplicationUser lp = getLowerPlayer(players, numberOfGamesInRound + i);					
					if(lp == null) {
						g = new Game(gameNumber, hp, lp, hp);
					} else {
						g = new Game(gameNumber, hp, lp, null);
					}
					g.setTournamentGame(tournament);
					gameRepository.save(g);
					gameNumber++;
					games.add(g);
				}
			} else {
				for(int i = 0; i < numberOfGamesInRound; i++) {					
					ApplicationUser hp = null;
					ApplicationUser lp = null;
					g = new Game(gameNumber, hp, lp, null, games.get(currOldGameIdx), games.get(currOldGameIdx + 1));
					g.setTournamentGame(tournament);
					if(games.get(currOldGameIdx).getWinner() != null)
						g.setPlayerOne(games.get(currOldGameIdx).getWinner());
					if(games.get(currOldGameIdx + 1).getWinner() != null)
						g.setPlayerTwo(games.get(currOldGameIdx + 1).getWinner());
					gameRepository.save(g);
					gameNumber++;
					currOldGameIdx += 2;
					games.add(g);
				}
			}			
		}
		tournament.setLadderGenerated(true);
		return games;
	}	

	private ApplicationUser getHigherPlayer(List<Point> players, int idx) {
		return players.get(idx).getParticipant();
	}

	private ApplicationUser getLowerPlayer(List<Point> players, int idx) {
		ApplicationUser p;
		try {
			p = players.get(idx).getParticipant();
		} catch (IndexOutOfBoundsException e) {
			p = null;
		}
		return p;
	}
	
	public List<Game> getGames(Tournament tournament) {
		List<Game> games = gameRepository.findGamesByTournamentGame(tournament);
		int oldId = -1;
		for (Game game : games) {			
			if(game.getOldOneId() != null) {
				oldId = game.getOldOneId();
				for (Game game2 : games) {
					if(game2.getId() == oldId) {
						game.setOldOne(game2);
					}
				}
			}
			if(game.getOldTwoId() != null) {
				oldId = game.getOldTwoId();
				for (Game game2 : games) {
					if(game2.getId() == oldId) {
						game.setOldTwo(game2);
					}
				}
			}
		}
		return games;
	}
	
	@Transactional
	public void updateGames(int id, int gameNumber, String winnerEmail) {
		Tournament tournament = tournamentRepository.findById(id).orElse(null);
		ApplicationUser winner = userRepository.findByEmail(winnerEmail).orElse(null);
		List<Game> games = gameRepository.findGamesByTournamentGame(tournament);
		int oldGameId = -1;
		for(Game game : games) {
			if(game.getGameNumber() == gameNumber) {
				game.setWinner(winner);
				oldGameId = game.getId();
				break;
			}			
		}
		for(Game game : games) {
			if(game.getOldOneId() != null && game.getOldOneId() == oldGameId) {
				game.setPlayerOne(winner);				
			}
			if(game.getOldTwoId() != null && game.getOldTwoId() == oldGameId) {
				game.setPlayerTwo(winner);				
			}
		}
		gameRepository.saveAll(games);
		tournament.setGames(games);		
	}
	
	@Transactional
	public void updateTournament(TournamentDto tournamentDto) {
		Tournament tournament = tournamentRepository.findById(tournamentDto.getId()).get();
		ApplicationUser applicationUser = userRepository.findByEmail(tournamentDto.getOrganizerEmail()).get();
		
		if(!tournament.getName().equals(tournamentDto.getName())) {
			tournament.setName(tournamentDto.getName());
		}
		if(tournament.getOrganizer().getId() != applicationUser.getId()) {
			tournament.setOrganizer(applicationUser);
		}		
	}
	
	public Optional<Tournament> findById(Integer id) {
		return tournamentRepository.findById(id);
	}
}
