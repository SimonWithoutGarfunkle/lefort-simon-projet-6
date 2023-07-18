package com.openclassrooms.paymybuddy.serviceTest;

import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

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
	
	@BeforeEach
	public void setUp() {
		utilisateurTest = new Utilisateur();
		
	}
	
	
	@Test
	public void addUtilisateurServiceTest() {
		//Arrange
		when(utilisateurRepository.save(utilisateurTest)).thenReturn(utilisateurTest);
				
		//Act
		utilisateurService.addUtilisateur(utilisateurTest);
		
		//Assert
		assertNotNull(utilisateurTest.getComptePMB().getComptePMBId());
		
	}


}
