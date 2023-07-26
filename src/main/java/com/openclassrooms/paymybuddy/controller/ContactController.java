package com.openclassrooms.paymybuddy.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.ContactRepository;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;
import com.openclassrooms.paymybuddy.service.ContactService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/contacts")
public class ContactController {
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	private static Logger logger = LoggerFactory.getLogger(ContactController.class);
	
	@GetMapping
	public String getContacts(Model model) {
		logger.info("performing get contact");
		return findPaginated(1, "prenom", "asc", model);
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, Model model) {
		int pageSize = 5;
		Page<Contact> page = contactService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Contact> listContacts = page.getContent();
		logger.info("la liste contacts contient : "+ listContacts.size());
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listContacts", listContacts);
		return "contacts";
		
		
		
	}
	
	@GetMapping("/contactForm")
	public String getContactForm(Model model) {
		logger.info("Appel de contactForm");
		
	    Contact newContact = new Contact();
	    model.addAttribute("contact", newContact);
		
		return "contactForm";
	}
	
	@GetMapping("/contactForm/{contactId}")
	public String getUpdateContactForm(@PathVariable int contactId, Model model) {
	    logger.info("Appel de getUpdateContactForm");

	    Contact existingContact = contactRepository.findById(contactId);

	    model.addAttribute("contact", existingContact);

	    return "contactForm";
	}
	
	@PostMapping("/contactForm/save")
	public String postContact(@ModelAttribute("contact") @Valid Contact contact, BindingResult bindingResult, Model model) {
		logger.info("Post save contactForm");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        
        contact.setUtilisateur(utilisateur);
        contactService.saveContact(contact);
		
		return "redirect:/contacts";
	}

}
