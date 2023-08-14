package com.openclassrooms.paymybuddy.controllerTest;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ContactControllerTest {
	
	@Autowired
	public MockMvc mockMvc;
	
	
	@Test
	@WithMockUser
	public void testGetContacts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/contacts").with(user(new Utilisateur(
        	    999,
        	    "test@example.com",
        	    "testpassword",
        	    "Doe",
        	    "John",
        	    Collections.singletonList(new Contact("ContactTest", "ContactPrenomTest")),
        	    new RIB(),
        	    new ComptePMB(),
        	    RoleUtilisateur.USER
        	))))
            .andExpect(MockMvcResultMatchers.status().isOk()); 
    }
	
		 @Test
		 @WithMockUser
		 public void testGetContactForm() throws Exception {
		     mockMvc.perform(MockMvcRequestBuilders.get("/contacts/new"))
		            .andExpect(MockMvcResultMatchers.status().isOk())
		            .andExpect(MockMvcResultMatchers.view().name("contactForm"))
		            .andExpect(MockMvcResultMatchers.model().attributeExists("contact"));
	    }
		 
		 /*
		 @Test
		 @WithMockUser
		 public void testgetUpdateContactForm() throws Exception {
		     mockMvc.perform(MockMvcRequestBuilders.get("/contacts/new"))
		            .andExpect(MockMvcResultMatchers.status().isOk())
		            .andExpect(MockMvcResultMatchers.view().name("contactForm"))
		            .andExpect(MockMvcResultMatchers.model().attributeExists("contact"));
	    }
		 
		 
		 
	 
	 	@Test
	    @WithMockUser
	    public void testPostContact_ValidContact() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/contacts/contactForm")
	            .param("email", "test@example.com")
	            .param("otherContactProperties", "otherValues")
	            .with(user(new Utilisateur(
	            	    999,
	            	    "test@example.com",
	            	    "testpassword",
	            	    "Doe",
	            	    "John",
	            	    Collections.singletonList(new Contact("ContactTest", "ContactPrenomTest")),
	            	    new RIB(),
	            	    new ComptePMB(),
	            	    RoleUtilisateur.USER
	            	))))
	        .andExpect(status().isCreated())
	        .andExpect(MockMvcResultMatchers.view().name("contacts"));
	    }

	    @Test
	    @WithMockUser
	    public void testPostContact_InvalidContact() throws Exception {
	        mockMvc.perform(MockMvcRequestBuilders.post("/contacts/contactForm")
	            .param("email", "")
	            .param("otherContactProperties", "otherValues")
	        )
	        .andExpect(MockMvcResultMatchers.view().name("contactForm"))
	        .andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"));
	    }*/
	 
	

}
