package com.github.pgleska.TournamentApp.util;

import java.util.Comparator;

import com.github.pgleska.TournamentApp.model.Point;

public class PointComparator implements Comparator<Point> {

	@Override
	public int compare(Point first, Point second) {
		return Integer.compare(first.getRankingPosition(), second.getRankingPosition());
	}
}
