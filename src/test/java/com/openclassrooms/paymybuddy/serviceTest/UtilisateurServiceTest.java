package com.openclassrooms.paymybuddy.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceTest {
	
	@InjectMocks
	private UtilisateurService utilisateurService;
	
	private Utilisateur utilisateurTest;
	
	@Mock
	private UtilisateurRepository utilisateurRepository;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@BeforeEach
	public void setUp() {
		utilisateurTest = new Utilisateur();
		
	}
	
	@Test
	public void getUtilisateurByEmailTest() {
		//Arrange
		when(utilisateurRepository.findByEmail("test@test.fr")).thenReturn(utilisateurTest);
		utilisateurTest.setEmail("test@test.fr");
		utilisateurTest.setPrenom("prenomTest");
				
		//Act
		utilisateurService.getUtilisateurByEmail("test@test.fr");
		
		//Assert
		assertEquals("prenomTest", utilisateurTest.getPrenom());
		
	}
	
	
	@Test
	public void addUtilisateurServiceTest() {
		//Arrange
		when(utilisateurRepository.save(utilisateurTest)).thenReturn(utilisateurTest);
		when(passwordEncoder.encode(utilisateurTest.getMotDePasse())).thenReturn(utilisateurTest.getMotDePasse());
				
		//Act
		utilisateurService.addUtilisateur(utilisateurTest);
		
		//Assert
		assertNotNull(utilisateurTest.getComptePMB().getComptePMBId());
		
	}


}
