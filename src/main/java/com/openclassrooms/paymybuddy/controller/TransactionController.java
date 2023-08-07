package com.openclassrooms.paymybuddy.controller;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@GetMapping
	public String getTransactions(Model model) {
		logger.info("Appel de la page Transactions");
		
		//return "transactions";
		return findPaginatedTransactionsController(1, "horodatage" ,"asc", model);
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginatedTransactionsController(@PathVariable(value = "pageNo") int pageNo, @RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir, Model model) {
		logger.info("Appel de findPaginatedTransactionsController");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
		Utilisateur utilisateurAJour = utilisateurService.getUtilisateurByEmail(utilisateur.getEmail());
		model.addAttribute("utilisateur", utilisateurAJour);
		
		int pageSize = 5;
		Page<Transaction> page = transactionService.findPaginatedTransactions(pageNo, pageSize, sortField, sortDir);
		List<Transaction> listTransactions = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());

		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		model.addAttribute("listTransactions", listTransactions);
		
		return "transactions";
	}
	
	
	@PostMapping
	public String moneyTransfert(@RequestParam BigDecimal montant, @RequestParam String action, @RequestParam String email, Model model) {
		logger.info("Demande de transfert d'argent");
		int result;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
		result = transactionService.moneyTransfertService(montant, action, email, utilisateur);
		
		if (result == 1) {
			String Message = "Argent envoyé ! ";
			model.addAttribute("infoMessage", Message);			
		} else if(result == 2) {
			String Message = "Argent recu !";
			model.addAttribute("infoMessage", Message);	
		} else if(result == 3) {
			String Message = "Action non prise en charge";
			model.addAttribute("errorMessage", Message);	
		} else if(result == 4) {
			String Message = "Votre solde est insuffisant";
			model.addAttribute("errorMessage", Message);	
		} else {
			String Message = "La demande n'a pas pu aboutir";
			model.addAttribute("errorMessage", Message);
		}
		
		Utilisateur utilisateurAJour = utilisateurService.getUtilisateurByEmail(utilisateur.getEmail());
		model.addAttribute("utilisateur", utilisateurAJour);
	
		return findPaginatedTransactionsController(1, "horodatage" ,"asc", model);
	}

}
