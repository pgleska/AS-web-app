package com.github.pgleska.TournamentApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.pgleska.TournamentApp.model.ApplicationUser;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long> {	
	Optional<ApplicationUser> findByEmail(String email);
}
