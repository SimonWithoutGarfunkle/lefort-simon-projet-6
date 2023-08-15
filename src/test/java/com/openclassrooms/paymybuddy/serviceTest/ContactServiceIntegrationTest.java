package com.openclassrooms.paymybuddy.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.service.ContactService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
public class ContactServiceIntegrationTest {
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	Contact contactTest = new Contact("contactTest", "contact@test.fr");
	Utilisateur utilisateurTest = new Utilisateur();
	
	@BeforeEach
	public void setUp() {
		utilisateurTest.setEmail("utilisateur@test.fr");
		utilisateurTest.setNom("utilisateurTest");
		utilisateurTest.setPrenom("utilisateurTest");
		utilisateurTest.setMotDePasse(passwordEncoder.encode("password123"));
		utilisateurTest = utilisateurService.addUtilisateur(utilisateurTest);					    
	    	
	}
	
	@Test
	public void addContactTest() {
		//Arrange
		List<Contact> resultBefore = contactService.getAllContacts();
		
		//Act
		contactService.addContact(utilisateurTest, contactTest);
		List<Contact> resultAfter = contactService.getAllContacts();
		
		
		//Assert
		assertEquals(resultBefore.size()+1, resultAfter.size());
		
	}
	
	@Test
	public void updateContactTest() {
		//Arrange
		contactService.addContact(utilisateurTest, contactTest);
		contactTest = contactService.getAllContactsByUserId(utilisateurTest.getUtilisateurId()).get(0);
		Integer contactTestId = contactTest.getContactId();				
		contactTest.setNom("nouveauNom");
		
		//Act
		contactService.updateContact(utilisateurTest, contactTest);
		
		//Assert
		assertTrue(contactRepository.getContactById(contactTestId).getNom()=="nouveauNom");
		
		
	}
	
	/*
	 * pas fini/le verify ne fonctionne que pour les mock
	@Test
	public void updateContactTestWrongUtilisateur() {
		//Arrange
		Utilisateur utilisateurTest2 = new Utilisateur();
		utilisateurTest2.setEmail("utilisateur2@test.fr");
		utilisateurTest2.setNom("utilisateurTest2");
		utilisateurTest2.setPrenom("utilisateurTest2");
		utilisateurTest2.setMotDePasse(passwordEncoder.encode("password123"));
		utilisateurService.addUtilisateur(utilisateurTest2);
		
		contactService.addContact(utilisateurTest, contactTest);
		contactTest = contactService.getAllContactsByUserId(utilisateurTest.getUtilisateurId()).get(0);		
		contactTest.setNom("nouveauNom");
		
		//Act
		contactService.updateContact(utilisateurTest2, contactTest);
		
		//Assert
		verify(contactRepository.save(contactTest), times(0));
		
		
	}*/
	
	@Test
	public void deleteContactTest() {
		//Arrange
		contactService.addContact(utilisateurTest, contactTest);
		contactTest = contactService.getAllContactsByUserId(utilisateurTest.getUtilisateurId()).get(0);
		Integer contactTestId = contactTest.getContactId();	
		
		//Act
		contactService.deleteContact(contactTestId);
		
		//Assert
		assertNull(contactRepository.getContactById(contactTestId));
		
	}
	
	
	

}
