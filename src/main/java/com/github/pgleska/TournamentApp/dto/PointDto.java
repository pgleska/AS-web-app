package com.github.pgleska.TournamentApp.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class PointDto {

	@Min(1)
	@Max(2147483)
	private Integer licenseNumber;
	
	@Min(1)
	@Max(2147483)
	private Integer rankingPosition;
	
	public Integer getLicenseNumber() {
		return licenseNumber;
	}
	
	public Integer getRankingPosition() {
		return rankingPosition;
	}
	
	public void setLicenseNumber(Integer licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	
	public void setRankingPosition(Integer rankingPosition) {
		this.rankingPosition = rankingPosition;
	}
}
