package com.github.pgleska.TournamentApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.pgleska.TournamentApp.service.MyUserDetailsService;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    DaoProvider daoAuthenticationProvider = new DaoProvider();
	    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
	    daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		
	    auth.authenticationProvider(daoAuthenticationProvider);
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/user/registration").permitAll()
				.antMatchers("/user/registration/**").permitAll()
				.antMatchers("/", "/message", "/tournaments", "/tournaments/{id:\\d+}").permitAll()
				.antMatchers("/error").permitAll()
				.antMatchers("/static/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/img/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.formLogin()
	            .loginPage("/login")
	            .failureUrl("/loginfailed")
	            .permitAll()
	         .and()
	         	.logout()
	         		.invalidateHttpSession(true)
			.and()
				.exceptionHandling().accessDeniedPage("/accessdenied");	
    }
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
