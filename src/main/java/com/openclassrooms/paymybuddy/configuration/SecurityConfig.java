package com.openclassrooms.paymybuddy.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	
	@SuppressWarnings("deprecation")
	@Bean
	public InMemoryUserDetailsManager userDetailsManager() {
		
		logger.info("userDetailsManager call");
		
		User.UserBuilder users = User.withDefaultPasswordEncoder();
		
		UserDetails userOne = users.username("testUserOne").password("passwordOne").roles("USER").build();
		UserDetails userTwo = users.username("testUserTwo").password("passwordTwo").roles("ADMIN").build();
		UserDetails userThree = users.username("user").password("aaa").roles("ADMIN").build();
		
		return new InMemoryUserDetailsManager(userOne, userTwo, userThree);
		
	}
	
	@SuppressWarnings({ "deprecation", "removal" })
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("SecurityFilterChain call");
		http.authorizeRequests((req) -> req.requestMatchers("/dashboard").authenticated()
											.anyRequest().permitAll())
											.formLogin().loginPage("/login")
											.usernameParameter("email")
							                .passwordParameter("motdepasse");
		
		return http.build();
	}
	



}
