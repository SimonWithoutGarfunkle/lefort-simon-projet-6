package com.openclassrooms.paymybuddy.serviceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.service.ContactService;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
	
	@InjectMocks
	private ContactService contactService;
	
	@Mock
	private ContactRepository contactRepository;
	
	private Utilisateur utilisateurTest;
	
	private Contact contactTest;
	
	private List<Contact> contactsUtilisateurTest;
	
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	
	@BeforeEach
	public void setUp() {
		utilisateurTest = new Utilisateur();
		utilisateurTest.setNom("utilisateurTest");
		utilisateurTest.setEmail("utilisateur@test.fr");
		contactsUtilisateurTest = new ArrayList<Contact>();
		utilisateurTest.setContacts(contactsUtilisateurTest);
		contactTest = new Contact();
		contactTest.setEmail("contact@test.fr");
		contactTest.setNom("contactTest");
				
	}
	
	
	
	@Test
	public void matchUtilisateurContactTest() {
		//Arrange
		contactsUtilisateurTest.add(contactTest);
		utilisateurTest.setContacts(contactsUtilisateurTest);
		when(contactRepository.findByUtilisateurId(any(Integer.class))).thenReturn(contactsUtilisateurTest);		
		
		//Act
		boolean result = contactService.matchUtilisateurContact(utilisateurTest, contactTest);
		
		//Assert
		assertTrue(result);
	}
	
	
	@Test
	public void matchUtilisateurContactTestFalse() {
		//Arrange
		Contact otherContact = new Contact();
		otherContact.setContactId(99);
		otherContact.setNom("othercontact");
		otherContact.setEmail("other@email.test");
		contactsUtilisateurTest.add(otherContact);
		utilisateurTest.setContacts(contactsUtilisateurTest);
		when(contactRepository.findByUtilisateurId(any(Integer.class))).thenReturn(contactsUtilisateurTest);
		
		
		//Act
		boolean result = contactService.matchUtilisateurContact(utilisateurTest, contactTest);
		
		//Assert
		assertFalse(result);
	}
	
	@Test
    public void findPaginatedTest() {
        // Arrange
        int pageNo = 1;
        int pageSize = 10;
        String sortField = "nom";
        String sortDirection = "ASC";
        
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Nom1", "email1@test.com"));
        contacts.add(new Contact("Nom2", "email2@test.com"));
        contacts.add(new Contact("Nom3", "email3@test.com"));
        contacts.add(new Contact("Nom4", "email4@test.com"));
        
        Page<Contact> contactPage = new PageImpl<>(contacts);

        when(contactRepository.findByUtilisateurId(anyInt(), any(Pageable.class))).thenReturn(contactPage);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(new Utilisateur());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        Page<Contact> result = contactService.findPaginatedContacts(pageNo, pageSize, sortField, sortDirection);

        // Assert
        verify(contactRepository, times(1)).findByUtilisateurId(anyInt(), any(Pageable.class));
        // Vérifiez que le résultat contient le bon nombre de contacts
        assertEquals(contacts.size(), result.getTotalElements());
        // Vérifiez que le premier contact du résultat correspond au premier contact de la liste factice
        assertEquals(contacts.get(0).getNom(), result.getContent().get(0).getNom());
        assertEquals(contacts.get(0).getEmail(), result.getContent().get(0).getEmail());
        assertEquals(contacts.get(1).getNom(), result.getContent().get(1).getNom());
        assertEquals(contacts.get(1).getEmail(), result.getContent().get(1).getEmail());
    }
	
	

}
