package com.github.pgleska.TournamentApp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pgleska.TournamentApp.dto.TournamentDto;
import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.Game;
import com.github.pgleska.TournamentApp.model.Point;
import com.github.pgleska.TournamentApp.model.Tournament;
import com.github.pgleska.TournamentApp.repository.TournamentRepository;
import com.github.pgleska.TournamentApp.repository.UserRepository;
import com.github.pgleska.TournamentApp.service.TournamentService;

@Controller
public class TournamentController {
	private final TournamentRepository tournamentRepository;
	private final TournamentService tournamentService;
	private final UserRepository userRepository;
	
	public TournamentController(TournamentRepository tournamentRepository, TournamentService tournamentService,
			UserRepository userRepository) {
		this.tournamentRepository = tournamentRepository;
		this.tournamentService = tournamentService;
		this.userRepository = userRepository;
	}
	
	@GetMapping("/tournaments")
	public String tournaments(Model model, @RequestParam(value = "page", required = false) Integer pageIdx,
			@RequestParam(value = "name", required = false) String name) {
		if(name != null) {
			List<Tournament> tournaments = tournamentRepository.findByNameContains(name);
			model.addAttribute("tournaments", tournaments);
			return "tournaments";
		} else {					
			if(pageIdx == null || pageIdx < 0)
				pageIdx = 0;
			Pageable pagerequest = PageRequest.of(pageIdx, 10);
			Page<Tournament> page = tournamentRepository.findAll(pagerequest);
			
			int nextPage = pageIdx + 1, previousePage = pageIdx - 1;
			boolean nextBlocked = false, previousBlocked = false;
			if(page.getTotalPages() <= pageIdx + 1) {
				nextBlocked = true;
			}
			if(pageIdx == 0) {
				previousBlocked = true;
			}
			
			List<Tournament> tournaments = page.getContent();
			model.addAttribute("tournaments", tournaments);
			model.addAttribute("nextPage", nextPage);
			model.addAttribute("nextBlocked", nextBlocked);
			model.addAttribute("previousPage", previousePage);
			model.addAttribute("previousBlocked", previousBlocked);
			return "tournaments";
		}
	}
	
	@GetMapping("/tournaments/{id}")
	public String detailedTournament(Principal principal, Model model, @PathVariable Integer id) {
		Tournament tournament = tournamentRepository.findById(id).orElse(null);
		String link = "https://www.google.com/maps/search/?api=1&query=" + tournament.getLatitude() + "%2C" + tournament.getLongitude();
		Boolean disabled = false;
		LocalDate today = LocalDate.now();
		List<Game> games = null;
		if(tournament.getParticipants() == tournament.getMaxNumberOfParticipants() || today.isAfter(tournament.getStartingDate()))
			disabled = true;				
		if(!tournament.getLadderGenerated())
			games = tournamentService.createLadder(tournament);
		else
			games = tournamentService.getGames(tournament);
		
		String tournamentWinner = "";
		if(!games.isEmpty()) {
			ApplicationUser winner = games.get(games.size() - 1).getWinner();
			if(winner != null) {
				tournamentWinner = winner.getFirstName() + " " + winner.getLastName();
			}
		}
		
		model.addAttribute("tournament", tournament);
		model.addAttribute("link", link);
		model.addAttribute("disabled", disabled);
		model.addAttribute("games", games);
		model.addAttribute("tournamentWinner", tournamentWinner);
		return "detailed_tournament";
	}
	
	@GetMapping("/tournaments/{id}/join")
	public String getJoinTournament(Principal principal, Model model, @PathVariable Integer id) {
		Tournament tournament = tournamentRepository.findById(id).orElse(null);
		Point point = new Point();
		model.addAttribute("point", point);
		model.addAttribute("tournament", tournament);
		return "join_tournament";
	}
	
	@PostMapping("/tournaments/{id}/join")
	public String postJoinTournament(Principal principal, Model model, @PathVariable Integer id, @ModelAttribute("point") Point point) {
		ApplicationUser requester = userRepository.findByEmail(principal.getName()).orElse(null);		
		String result = tournamentService.joinToTournament(id, requester, point);
		int code = -1;
		switch(result) {
			case "success":
				code = 2;
				break;
			case "present":
				code = 3;
				break;
			case "full":
				code = 4;
				break;
				
		}
		return "redirect:/message?code=" + code;
	}
	
	@GetMapping("/tournaments/create")
	public String getCreateTournament(Model model) {
		TournamentDto tournament = new TournamentDto();
		model.addAttribute("tournament", tournament);
		return "create_tournament";
	}
	
	@PostMapping("/tournaments/create")
	public String createTournament(@ModelAttribute("tournament") TournamentDto tournament, Principal principal) {
		ApplicationUser requester = userRepository.findByEmail(principal.getName()).orElse(null);
		
		tournamentService.createTournament(tournament, requester);
		
		return "redirect:/tournaments";
	}
	
	@GetMapping("/tournaments/upcoming")
	public String getUpcomingTournaments(Model model, Principal principal) {
		ApplicationUser organizer = userRepository.findByEmail(principal.getName()).orElse(null);
		List<Tournament> tournaments = tournamentRepository.findAll();
		List<Tournament> filtered = tournaments.stream().filter(t -> t.getMembers().contains(organizer)).collect(Collectors.toList());
		model.addAttribute("tournaments", filtered);
		return "upcoming_tournaments";
	}
	
	@PostMapping(value = "/tournaments/{id}/winner")
	public String postWinner(Principal principal, Model model, @RequestParam Map<String, String> body, @PathVariable Integer id) {
		ApplicationUser requester = userRepository.findByEmail(principal.getName()).orElse(null);
		Tournament tournament = tournamentRepository.findById(id).orElse(null);
		if(requester.getId() != tournament.getOrganizer().getId())
			return "redirect:/message?code=1";
		if(!body.containsKey("winner"))
			return "redirect:/message?code=5";
		String[] args = body.get("winner").split(";");
		int gameNumber = Integer.valueOf(args[0]);		
		String winner = args[1];
		tournamentService.updateGames(id, gameNumber, winner);
		return "redirect:/tournaments/" + id;
	}
}
