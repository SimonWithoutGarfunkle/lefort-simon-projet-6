package com.openclassrooms.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

@RestController
public class TestRestController {
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	@GetMapping(value = "/utilisateurs", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Utilisateur>> getAllUtilisateurs() {
		Iterable<Utilisateur> utilisateurs = utilisateurService.getAllUtilisateurs();
		return ResponseEntity.ok(utilisateurs);
	}
	
	@GetMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Iterable<Transaction>> getAllTransactions() {
		Iterable<Transaction> transactions = transactionService.getAllTransactions();
		return ResponseEntity.ok(transactions);
	}

}