package com.openclassrooms.paymybuddy.controllerTest;

import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;
import org.junit.jupiter.api.Test;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UtilisateurControllerTest {
	
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
		BigDecimal initialAmount = new BigDecimal(200.0);
		transactionService.creditFromBankAccount(initialAmount, utilisateurTest);
						
		
	}
	
	@Test
	@WithMockUser
	public void testGetDashboard() throws Exception {
	     mockMvc.perform(MockMvcRequestBuilders.get("/dashboard")
	    		 .with(user(utilisateurTest)))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(MockMvcResultMatchers.view().name("dashboard"));
   }
	
	@Test
	public void testGetRegister() throws Exception {
	     mockMvc.perform(MockMvcRequestBuilders.get("/register"))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(MockMvcResultMatchers.view().name("register"))
	            .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"));
   }	
	
	@Test
	@WithMockUser
	public void testGetProfil() throws Exception {	
		mockMvc.perform(MockMvcRequestBuilders.get("/profil")
	    		 .with(user(utilisateurTest)))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(MockMvcResultMatchers.view().name("profil"))
	            .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"));
		
	}
	
	@Test
	@WithMockUser
	public void testGetProfilWithInfoMessage() throws Exception {
	    String infoMessage = "Info message here";
	    mockMvc.perform(MockMvcRequestBuilders.get("/profil")
	            .with(user(utilisateurTest))
	            .flashAttr("infoMessage", infoMessage))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(MockMvcResultMatchers.view().name("profil"))
	            .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
	            .andExpect(MockMvcResultMatchers.model().attribute("infoMessage", infoMessage));
	}

	@Test
	@WithMockUser
	public void testGetProfilWithErrorMessage() throws Exception {
	    String errorMessage = "Error message here";
	    mockMvc.perform(MockMvcRequestBuilders.get("/profil")
	            .with(user(utilisateurTest))
	            .flashAttr("errorMessage", errorMessage))
	            .andExpect(MockMvcResultMatchers.status().isOk())
	            .andExpect(MockMvcResultMatchers.view().name("profil"))
	            .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
	            .andExpect(MockMvcResultMatchers.model().attribute("errorMessage", errorMessage));
	}
	
	@Test
    @WithMockUser()
    public void testUpdateProfile() throws Exception {
        utilisateurTest.setNom("updatedName");

        mockMvc.perform(MockMvcRequestBuilders.post("/profil")
                .with(user(utilisateurTest))
                .flashAttr("utilisateur", utilisateurTest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profil"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("infoMessage"));
    }
	
	
	
	
	@Test
    @WithMockUser
    public void testDebitToBankAccount() throws Exception {
        BigDecimal montant = new BigDecimal("100.00");

        mockMvc.perform(MockMvcRequestBuilders.post("/profil/banking")
                .with(user(utilisateurTest))
                .param("montant", montant.toString())
                .param("action", "debit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profil"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("infoMessage"));
    }
	
	@Test
    @WithMockUser
    public void testDebitToBankAccountNotEnoughMoney() throws Exception {
        BigDecimal montant = new BigDecimal("500.00");

        mockMvc.perform(MockMvcRequestBuilders.post("/profil/banking")
                .with(user(utilisateurTest))
                .param("montant", montant.toString())
                .param("action", "debit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profil"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"));
    }

    @Test
    @WithMockUser
    public void testCreditFromBankAccount() throws Exception {
        BigDecimal montant = new BigDecimal("50.00");

        mockMvc.perform(MockMvcRequestBuilders.post("/profil/banking")
                .with(user(utilisateurTest))
                .param("montant", montant.toString())
                .param("action", "credit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profil"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("utilisateur"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("infoMessage"));
    }
    
    @Test
    @WithMockUser
    public void testbankingOperationWrongAction() throws Exception {
        BigDecimal montant = new BigDecimal("50.00");

        mockMvc.perform(MockMvcRequestBuilders.post("/profil/banking")
                .with(user(utilisateurTest))
                .param("montant", montant.toString())
                .param("action", "failureTest"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("profil"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("actionErrorMessage"));
    }
    
    @Test
    public void testConfirmRegister_SuccessfulRegistration() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("testUtilisateur@example.fr");
        utilisateur.setMotDePasse("testpasswordTest");
		utilisateur.setNom("DoeTest");
		utilisateur.setPrenom("JohnTest");

        mockMvc.perform(MockMvcRequestBuilders.post("/register/confirmRegister")
                .flashAttr("utilisateur", utilisateur))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name("redirect:/register/success"));
        
        assertTrue(!(utilisateurService.getUtilisateurByEmail("testUtilisateur@example.fr") == null));

    }
    
    @Test
    public void testConfirmRegister_ValidationErrors() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("invalid-email");


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/register/confirmRegister")
                .flashAttr("utilisateur", utilisateur))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"))
                .andReturn(); 

        ModelAndView modelAndView = result.getModelAndView();

        BindingResult bindingResult = (BindingResult) modelAndView.getModel()
                .get(BindingResult.MODEL_KEY_PREFIX + "utilisateur");

        FieldError emailError = bindingResult.getFieldError("email");
        Assertions.assertNotNull(emailError);
        Assertions.assertEquals("invalid-email", emailError.getRejectedValue());
    }

}







    
    

