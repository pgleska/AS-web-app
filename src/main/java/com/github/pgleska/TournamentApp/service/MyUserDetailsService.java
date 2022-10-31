package com.github.pgleska.TournamentApp.service;

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
import com.github.pgleska.TournamentApp.repository.UserRepository;

@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
 
    private final UserRepository userRepository;
    
    public MyUserDetailsService(UserRepository userRepository) {
    	this.userRepository = userRepository;
	}
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmail(username).orElse(null);

        if(user == null) {
        	throw new UsernameNotFoundException("No user found with username: " + username);
        }
        
        boolean enabled = user.getEnabled();
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        
        List<String> roles = new ArrayList<>();
        
        if(user.getRole().equals("admon"))
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
