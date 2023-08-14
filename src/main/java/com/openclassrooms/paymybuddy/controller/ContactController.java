package com.openclassrooms.paymybuddy.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.openclassrooms.paymybuddy.service.UtilisateurService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/contacts")
public class ContactController {

	@Autowired
	private ContactService contactService;

	@Autowired
	private UtilisateurService utilisateurService;

	private static Logger logger = LoggerFactory.getLogger(ContactController.class);

	@GetMapping
	public String getContacts(Model model) {
		logger.info("Appel get contact");
		return findPaginatedContactsController(1, "prenom", "asc", model);
	}

	@GetMapping("/page/{pageNo}")
	public String findPaginatedContactsController(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, Model model) {
		logger.info("Appel get contact");
		int pageSize = 5;
		Page<Contact> page = contactService.findPaginatedContacts(pageNo, pageSize, sortField, sortDir);
		List<Contact> listContacts = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listContacts", listContacts);
		return "contacts";

	}

	@GetMapping("/new")
	public String getContactForm(Model model) {
		logger.info("Appel de contactForm");

		Contact newContact = new Contact();
		model.addAttribute("contact", newContact);
		logger.info("contactId : " + newContact.getContactId());

		return "contactForm";
	}

	@GetMapping("/{contactId}/edit")
	public String getUpdateContactForm(@PathVariable Integer contactId, Model model) {
		logger.info("Appel de getUpdateContactForm");

		Contact existingContact = contactService.getContactByContactId(contactId);

		model.addAttribute("contact", existingContact);

		return "contactForm";
	}

	@Transactional
	@PostMapping("/contactForm")
	public String postContact(@ModelAttribute("contact") @Valid Contact contact, BindingResult bindingResult,
			Model model) {
		logger.info("Post save contactForm");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
		
		if (utilisateurService.getUtilisateurByEmail(contact.getEmail()) == null) {
			logger.error("l'email ne correspond a aucun utilisateur");
			model.addAttribute("errorMessage", "l'email ne correspond a aucun utilisateur");
			return "contactForm";

		}
		logger.info("contact id : "+contact.getContactId());
		
		if (contact.getContactId()!=null) {
			contactService.updateContact(utilisateur, contact);
		} else {
			contactService.addContact(utilisateur, contact);
		}
		

		return "redirect:/contacts";
	}
	

	@GetMapping("/{contactId}/delete")
	public String getDeleteForm(@PathVariable Integer contactId, Model model) {
		logger.info("Appel de deleteForm");
		Contact contactToDelete = contactService.getContactByContactId(contactId);

		model.addAttribute("contact", contactToDelete);
		return "deleteContactForm";
	}


	@Transactional
	@DeleteMapping("/{contactId}")
	public String deleteContact(@PathVariable Integer contactId, Model model) {
		logger.info("Appel de delete");
		contactService.deleteContact(contactId);

		return "redirect:/contacts";

	}
	
	

}
