package com.openclassrooms.paymybuddy.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.openclassrooms.paymybuddy.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("SecurityFilterChain call");
		http.authorizeHttpRequests((req) -> req.requestMatchers("/dashboard").authenticated()
											.anyRequest().permitAll())
							                .formLogin(form -> form
							                    .loginPage("/login")
							                    .usernameParameter("email")
								                .passwordParameter("motdepasse")
							                    .defaultSuccessUrl("/dashboard")
							                    .failureUrl("/login?error=true")
							                );
		
		return http.build();
	}
	
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
	
	@Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
     
    
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } 
	 

}
