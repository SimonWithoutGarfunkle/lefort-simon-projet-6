package com.openclassrooms.paymybuddy.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@Controller
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	private static Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@GetMapping("/transactions")
	public String getDashboard(Model model) {
		logger.info("performing get transactions");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
		model.addAttribute("utilisateur", utilisateur);
		return "transactions";
	}
	
	@PostMapping("/transactions")
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
	
		return "transactions";
	}

}
