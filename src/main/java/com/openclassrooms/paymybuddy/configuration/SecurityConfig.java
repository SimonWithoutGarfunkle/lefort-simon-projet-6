package com.openclassrooms.paymybuddy.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
	

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		logger.info("Appel deSecurityFilterChain");
		

		http.authorizeHttpRequests((req) -> req.requestMatchers("/dashboard").authenticated()
				.requestMatchers("/transactions").authenticated()
				.requestMatchers("/contacts").authenticated()
				.requestMatchers("/profil").authenticated()
											.anyRequest().permitAll())											
							                .formLogin(form -> form
							                    .loginPage("/login")
							                    .usernameParameter("email")
								                .passwordParameter("motdepasse")
							                    .defaultSuccessUrl("/dashboard")
							                    .failureUrl("/login?error")
							                )							                
							                .logout((logout) ->
							 					logout.logoutUrl("/logout")
							 						.deleteCookies("remove")
							 						.logoutSuccessUrl("/login")
							 						.invalidateHttpSession(true)
							 						.clearAuthentication(true)
							 						.permitAll())
							                .oauth2Login(oauth2Login ->
							                oauth2Login
									                .loginPage("login")
								                    .defaultSuccessUrl("http://localhost:8080/paymybuddy/dashboard"));
									                
		
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
    public UtilisateurService userDetailsService() {
        return new UtilisateurService();
    }
     
    
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } 
	 

}
