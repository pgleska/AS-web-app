package com.github.pgleska.TournamentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.Tournament;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Integer> {
	List<Tournament> findByOrganizer(ApplicationUser organizer);
	List<Tournament> findByNameContains(String name);
}
