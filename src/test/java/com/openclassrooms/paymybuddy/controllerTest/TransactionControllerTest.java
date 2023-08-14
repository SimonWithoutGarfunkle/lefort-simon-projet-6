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
	
	@BeforeEach
	public void setUp() {
		//Pour les tests l'utilisateur test est son propre contact
		contact = new Contact();
		contact.setEmail("test@example.com");
		contact.setNom("Doe");
		contact.setPrenom("John");
		contact.setSurnom("surnomContact");
		contact.setCommentaire("comment");
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(contact);
		utilisateurTest = new Utilisateur();
		utilisateurTest.setEmail("test@example.com");
		utilisateurTest.setMotDePasse("testpassword");
		utilisateurTest.setNom("Doe");
		utilisateurTest.setPrenom("John");
		utilisateurTest.setContacts(contacts);
		
		utilisateurService.addUtilisateur(utilisateurTest);
		BigDecimal initialAmount = new BigDecimal(200.0);
		transactionService.creditFromBankAccount(initialAmount, utilisateurTest);
						
		
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

}
