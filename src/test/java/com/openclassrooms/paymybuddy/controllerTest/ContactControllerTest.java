package com.openclassrooms.paymybuddy.controllerTest;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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

import com.openclassrooms.paymybuddy.model.ComptePMB;
import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.RIB;
import com.openclassrooms.paymybuddy.model.RoleUtilisateur;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.ContactService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ContactControllerTest {

	@Autowired
	public MockMvc mockMvc;

	private Utilisateur utilisateurTest;

	private Contact contact;

	private Integer contactId;

	@Autowired
	private UtilisateurService utilisateurService;

	@Autowired
	private ContactService contactService;

	@BeforeEach
	public void setUp() {
		contact = new Contact();
		contact.setEmail("emailContact");
		contact.setTelephone("telephone");
		contact.setNom("nomContact");
		contact.setPrenom("prenomContact");
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
		contactId = contact.getContactId();

	}

	@Test
	@WithMockUser
	public void testGetContacts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/contacts").with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@WithMockUser
	public void testfindPaginatedContactsController() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/contacts/page/1")
				.param("sortField", "surnom")
				.param("sortDir", "desc")
				.with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser
	public void testGetContactForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/contacts/new").with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("contactForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"));
	}

	@Test
	@WithMockUser
	public void testgetUpdateContactForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/contacts/" + contactId + "/edit").with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("contactForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"));
	}

	@Test
	@WithMockUser
	public void testgetDeleteContactForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/contacts/" + contactId + "/delete").with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("deleteContactForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("contact"));
	}

	@Test
	@WithMockUser
	public void testDeleteContact() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/contacts/" + contactId).with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/contacts"));

		assertNull(contactService.getContactByContactId(contactId));
	}

	@Test
	@WithMockUser
	public void testPostContact_ValidContact() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/contacts/contactForm").param("email", "test@example.com")
				.param("otherContactProperties", "otherValues").with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/contacts"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("errorMessage"));
	}

	@Test
	@WithMockUser
	public void testPostContact_UpdateContact() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/contacts/contactForm").param("contactId", contactId.toString())
				.param("email", "test@example.com").param("surnom", "nouveausurnomtest").with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/contacts"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("errorMessage"));
		Contact result = contactService.getContactByContactId(contactId);
		assertTrue(result.getSurnom().equals("nouveausurnomtest"));
	}

	@Test
	@WithMockUser
	public void testPostContact_InvalidContact() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/contacts/contactForm").param("email", "")
				.param("otherContactProperties", "otherValues").with(user(utilisateurTest)))
				.andExpect(MockMvcResultMatchers.view().name("contactForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"));
	}

}
