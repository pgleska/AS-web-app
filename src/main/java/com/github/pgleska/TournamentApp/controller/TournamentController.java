package com.github.pgleska.TournamentApp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pgleska.TournamentApp.dto.PointDto;
import com.github.pgleska.TournamentApp.dto.TournamentDto;
import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.Game;
import com.github.pgleska.TournamentApp.model.Tournament;
import com.github.pgleska.TournamentApp.repository.TournamentRepository;
import com.github.pgleska.TournamentApp.repository.UserRepository;
import com.github.pgleska.TournamentApp.service.TournamentService;
import com.github.pgleska.TournamentApp.service.UserService;

@Controller
public class TournamentController {
	
	private static final Logger logger = LoggerFactory.getLogger(TournamentController.class);
	
	private final TournamentRepository tournamentRepository;
	private final TournamentService tournamentService;
	private final UserRepository userRepository;
	private final UserService userService;
	
	private Pattern pattern;	
	
	public TournamentController(TournamentRepository tournamentRepository, TournamentService tournamentService,
			UserRepository userRepository, UserService userService) {
		this.tournamentRepository = tournamentRepository;
		this.tournamentService = tournamentService;
		this.userRepository = userRepository;
		this.userService = userService;
		
		pattern = Pattern.compile("^[0-9]+;[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");		
	}
	
	@GetMapping("/tournaments")
	public String tournaments(Model model, HttpSession session, 
			@RequestParam(value = "page", required = false) String pageIdxS,
			@RequestParam(value = "name", required = false) String name,
			Principal principal) {				
		
		boolean guest = true;
		if(Objects.nonNull(principal))
			guest = false;		
		model.addAttribute("guest", guest);		
		model.addAttribute("admin", userService.checkIfAdmin(principal.getName()));
		
		Integer pageIdx = 0;
		if(Objects.nonNull(pageIdxS)) {
			try {
				pageIdx = Integer.valueOf(pageIdxS);
			} catch (NumberFormatException e) {
				return "redirect:/tournaments";
			}
		}
		
		if(name != null) {
			List<Tournament> tournaments = tournamentRepository.findByNameContains(name);
			model.addAttribute("tournaments", tournaments);
			return "tournaments";
		} else {					
			if(pageIdx < 0)
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
	
	@GetMapping("/tournaments/{idS}")
	public String detailedTournament(Principal principal, Model model, @PathVariable String idS) {
		boolean guest = true;
		if(Objects.nonNull(principal))
			guest = false;		
		model.addAttribute("guest", guest);
		model.addAttribute("admin", userService.checkIfAdmin(principal.getName()));
		
		Integer id = 0;
		try {
			id = Integer.valueOf(idS);
		} catch (NumberFormatException e) {
			return "redirect:/message?code=9";
		}			
		Tournament tournament = tournamentRepository.findById(id).orElse(null);
		if(Objects.isNull(tournament))
			return "redirect:/message?code=8";
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
		boolean disabledSelect = true;
		if(Objects.nonNull(principal) && principal.getName().equals(tournament.getOrganizer().getEmail()))
			disabledSelect = false;
		TournamentDto tournamentDto = TournamentDto.convert(tournament);
		model.addAttribute("tournament", tournamentDto);
		model.addAttribute("link", link);
		model.addAttribute("disabled", disabled);
		model.addAttribute("games", games);
		model.addAttribute("tournamentWinner", tournamentWinner);
		model.addAttribute("disabledSelect", disabledSelect);
		return "detailed_tournament";
	}
	
	@GetMapping("/tournaments/{idS}/join")
	public String getJoinTournament(Principal principal, Model model, @PathVariable String idS) {
		Integer id = 0;
		try {
			id = Integer.valueOf(idS);
		} catch (NumberFormatException e) {
			return "redirect:/message?code=9";
		}			
		Tournament tournament = tournamentRepository.findById(id).orElse(null);
		if(Objects.isNull(tournament))
			return "redirect:/message?code=8";
		
		PointDto point = new PointDto();
		model.addAttribute("point", point);
		model.addAttribute("tournament", tournament);
		return "join_tournament";
	}
	
	@PostMapping("/tournaments/{idS}/join")
	public String postJoinTournament(Principal principal, Model model, @PathVariable String idS, 
			@Valid @ModelAttribute("point") PointDto point, BindingResult bindingResult) {
		ApplicationUser requester = userRepository.findByEmail(principal.getName()).orElse(null);
		Integer id = 0;
		try {
			id = Integer.valueOf(idS);
		} catch (NumberFormatException e) {
			return "redirect:/message?code=9";
		}					
		
		if(bindingResult.hasErrors()) {
			return "redirect:/message?code=13";
		}
		
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
			case "missing":
				code = 8;
				break;
				
		}
		return "redirect:/message?code=" + code;
	}
	
	@GetMapping("/tournaments/create")
	public String getCreateTournament(Model model, Principal principal) {
		model.addAttribute("admin", userService.checkIfAdmin(principal.getName()));
		TournamentDto tournament = new TournamentDto();
		model.addAttribute("tournament", tournament);
		return "create_tournament";
	}
	
	@PostMapping("/tournaments/create")
	public String createTournament(@Valid @ModelAttribute("tournament") TournamentDto tournament,  BindingResult bindingResult, Principal principal) {
		if(bindingResult.hasErrors()) 
			return "redirect:/message?code=10";
		ApplicationUser requester = userRepository.findByEmail(principal.getName()).orElse(null);
		tournamentService.createTournament(tournament, requester);
		return "redirect:/tournaments";
	}
	
	@GetMapping("/tournaments/upcoming")
	public String getUpcomingTournaments(Model model, Principal principal) {
		model.addAttribute("admin", userService.checkIfAdmin(principal.getName()));
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
		if(Objects.isNull(tournament))
			return "redirect:/message?code=8";
		if(requester.getId() != tournament.getOrganizer().getId())
			return "redirect:/message?code=1";
		if(!body.containsKey("winner"))
			return "redirect:/message?code=5";
		String seq = body.get("winner");
		Matcher matcher = pattern.matcher(seq);
		if(!matcher.matches())
			return "redirect:/message?code=11";
		String[] args = seq.split(";");
		int gameNumber = Integer.valueOf(args[0]);		
		String winner = args[1];
		ApplicationUser winnerUser = userRepository.findByEmail(principal.getName()).orElse(null);
		if(Objects.isNull(winnerUser))
			return "redirect:/message?code=12";
		tournamentService.updateGames(id, gameNumber, winner);
		return "redirect:/tournaments/" + id;
	}
}
