package com.github.pgleska.TournamentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.pgleska.TournamentApp.model.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {
	
}
