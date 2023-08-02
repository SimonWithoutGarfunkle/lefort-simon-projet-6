package com.openclassrooms.paymybuddy.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.paymybuddy.model.RoleUtilisateur;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class UtilisateurServiceIntegrationTest {
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	Utilisateur utilisateur1 = new Utilisateur();
	
	Utilisateur utilisateur2 = new Utilisateur();
	
	@BeforeEach
	public void setUp() {
		// Utilisateur 1
	    utilisateur1.setEmail("john.doe@test.com");
	    utilisateur1.setMotDePasse(passwordEncoder.encode("password123"));
	    utilisateur1.setNom("Doe");
	    utilisateur1.setPrenom("John");
	    utilisateur1.setRole(RoleUtilisateur.USER);

	    // Utilisateur 2
	    utilisateur2.setEmail("jane.smith@test.com");
	    utilisateur2.setMotDePasse(passwordEncoder.encode("password456"));
	    utilisateur2.setNom("Smith");
	    utilisateur2.setPrenom("Jane");
	    utilisateur2.setRole(RoleUtilisateur.USER);
	    
	    	
	}
	
	@Test
	public void addUtilisateurTI() {
		//Arrange
						
		//Act
		utilisateurService.addUtilisateur(utilisateur1);
		
		//Assert
		Utilisateur result = utilisateurRepository.findByEmail("john.doe@test.com");
		assertNotNull(result);
	
		
	}
	
	@Test
	public void addUtilisateurTIDoublon() {
		//Arrange
		utilisateurService.addUtilisateur(utilisateur1);
		utilisateur2.setEmail("john.doe@test.com");
				
		//Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	        utilisateurService.addUtilisateur(utilisateur2);
	    });
		
		//Assert
		String expectedMessage = "Cet e-mail est déjà enregistré";
	    String actualMessage = exception.getMessage();
	    assertTrue(actualMessage.contains(expectedMessage));
	
	}
	
	@Test
	public void loadUserByUsernameTI() {
		//Arrange
		utilisateurService.addUtilisateur(utilisateur1);
		
		//Act
		UserDetails userDetails = utilisateurService.loadUserByUsername("john.doe@test.com");
		
		//Assert
		  assertNotNull(userDetails);
		  assertEquals("john.doe@test.com", userDetails.getUsername());
		
	}
	
	@Test
	public void loadUserByUsernameTIWrongEmail() {
		assertThrows(UsernameNotFoundException.class, () -> {
            utilisateurService.loadUserByUsername("nonexisting@example.com");
        });
		
	}
}
