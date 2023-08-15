package com.openclassrooms.paymybuddy.controllerTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.ContactService;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TransactionControllerTest {
	
	@Autowired
	public MockMvc mockMvc;
	
	private Utilisateur utilisateurTest;
	
	private Contact contact;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	@Autowired
	private ContactService contactService;
	
	@BeforeEach
	public void setUp() {
		//Utilisation des valeurs pré chargées dans la base de test pour avoir un compte avec des contacts complets
		utilisateurTest = utilisateurService.getUtilisateurByEmail("simonlefort@hotmail.fr");
		contact = contactService.getContactByContactId(1);
						
		
	}
	
	@Test
	@WithMockUser
	public void testGetTransactions() throws Exception {
	    mockMvc.perform(MockMvcRequestBuilders.get("/transactions")
	    		.with(user(utilisateurTest)))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(MockMvcResultMatchers.view().name("transactions"))
	            .andExpect(MockMvcResultMatchers.model().attributeExists("currentPage", "totalPages", "totalItems", 
	                                              "sortField", "sortDir", "reverseSortDir",
	                                              "listTransactions", "utilisateur"));
	}
	
	@Test
	@WithMockUser
	public void testfindPaginatedTransactionsController() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/transactions/page/1")
				.param("sortField", "horodatage")
				.param("sortDir", "desc")
				.with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testMoneyTransfertSend() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
				.param("montant", "50")
				.param("action", "recevoir")
				.param("email", contact.getEmail())
				.with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("transactions"))
				.andExpect(MockMvcResultMatchers.model().attribute("infoMessage","Argent recu !"));
	}
	
	@Test
	@WithMockUser
	public void testMoneyTransfertSendNotEnough() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
				.param("montant", "500000")
				.param("action", "envoyer")
				.param("email", contact.getEmail())
				.with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("transactions"))
				.andExpect(MockMvcResultMatchers.model().attribute("errorMessage","Votre solde est insuffisant"));
	}
	
	@Test
	@WithMockUser
	public void testMoneyTransfertReceive() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
				.param("montant", "50")
				.param("action", "envoyer")
				.param("email", contact.getEmail())
				.with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("transactions"))
				.andExpect(MockMvcResultMatchers.model().attribute("infoMessage","Argent envoyé ! "));
	}
	
	@Test
	@WithMockUser
	public void testMoneyTransfertReceiveNotEnough() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
				.param("montant", "5000")
				.param("action", "recevoir")
				.param("email", contact.getEmail())
				.with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("transactions"))
				.andExpect(MockMvcResultMatchers.model().attribute("errorMessage","La demande n'a pas pu aboutir"));
	}
	
	@Test
	@WithMockUser
	public void testMoneyTransfertSendWrongAction() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
				.param("montant", "50")
				.param("action", "wrongAction")
				.param("email", contact.getEmail())
				.with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("transactions"))
				.andExpect(MockMvcResultMatchers.model().attribute("errorMessage","Action non prise en charge"));
	}

}
