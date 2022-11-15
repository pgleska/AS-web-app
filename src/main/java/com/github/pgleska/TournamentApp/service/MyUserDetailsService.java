package com.github.pgleska.TournamentApp.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.pgleska.TournamentApp.model.ApplicationUser;
import com.github.pgleska.TournamentApp.model.LoginAttempt;
import com.github.pgleska.TournamentApp.repository.LoginAttemptRepository;
import com.github.pgleska.TournamentApp.repository.UserRepository;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
 
    private final UserRepository userRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    
    public MyUserDetailsService(UserRepository userRepository, LoginAttemptRepository loginAttemptRepository) {
    	this.userRepository = userRepository;
    	this.loginAttemptRepository = loginAttemptRepository;
	}
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	ApplicationUser user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));            	
    	
        boolean enabled = user.isEnabled();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        
        List<LoginAttempt> loginAttempts = 
    			loginAttemptRepository.findFirst5ByUserIdAndStatusOrderByAttemptTimeDesc(user.getId(), 0);    	    	
        
        if(loginAttempts.size() >= 5 && ChronoUnit.HOURS.between(loginAttempts.get(4).getAttemptTime(), LocalDateTime.now()) < 1l) {
        	accountNonLocked = false;
        }
        
        List<String> roles = new ArrayList<>();
        
        if(user.getRole().equals("admin"))
        	roles.addAll(List.of("admin", "user"));
        else 
        	roles.add("user");
        
        return new User(
          user.getEmail(), user.getPassword(), enabled, accountNonExpired,
          credentialsNonExpired, accountNonLocked, getAuthorities(roles));
    }
    
    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
