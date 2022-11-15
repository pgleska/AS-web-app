package com.github.pgleska.TournamentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.pgleska.TournamentApp.model.LoginAttempt;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Integer> {
	List<LoginAttempt> findFirst5ByUserIdAndStatusOrderByAttemptTimeDesc(Long l, Integer status);

}
