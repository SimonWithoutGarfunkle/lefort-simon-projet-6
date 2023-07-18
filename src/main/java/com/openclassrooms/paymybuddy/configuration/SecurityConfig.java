package com.openclassrooms.paymybuddy.configuration;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
    private UserDetailsService userDetailsService;
	
	private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		logger.info("SecurityFilterChain applied");	
		
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers("/register").permitAll()
				.requestMatchers("/register/*").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/dashboard").authenticated()
				.requestMatchers("/*").permitAll())
				.formLogin((formLogin) -> formLogin.loginPage("/login"));
				

		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		logger.info("Encode password");
		return new BCryptPasswordEncoder();
	}

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
    	logger.info("DaoAuthenticationProvider call");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
    	logger.info("AuthenticationManager call");
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }

}
