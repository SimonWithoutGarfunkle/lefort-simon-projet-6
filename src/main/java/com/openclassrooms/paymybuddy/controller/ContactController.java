package com.openclassrooms.paymybuddy.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.paymybuddy.model.Contact;
import com.openclassrooms.paymybuddy.service.ContactService;

@Controller
@RequestMapping("/contacts")
public class ContactController {
	
	@Autowired
	private ContactService contactService;	
	
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

}
