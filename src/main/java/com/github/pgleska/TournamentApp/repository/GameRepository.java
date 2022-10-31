package com.github.pgleska.TournamentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.pgleska.TournamentApp.model.Game;
import com.github.pgleska.TournamentApp.model.Tournament;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
	List<Game> findGamesByTournamentGame(Tournament tournamentGame);	
}
