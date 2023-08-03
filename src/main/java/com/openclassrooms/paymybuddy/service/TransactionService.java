package com.openclassrooms.paymybuddy.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.ComptePMB;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.ComptePMBRepository;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private ComptePMBRepository comptePMBRepository;
	
	private static Logger logger = LoggerFactory.getLogger(TransactionService.class);
	
	/**
	 * Retourne toutes les transactions enregistrées pour des tests en phase de developpement
	 * 
	 * @return toutes les transactions enregistrées dans la base
	 */
	public Iterable<Transaction> getAllTransactions() {
		logger.info("Recuperation de toutes les transactions de la base");
		return transactionRepository.findAll();
	}
	
	public ComptePMB getComptePMB(Utilisateur utilisateur) {
		return comptePMBRepository.findByUtilisateurEmail(utilisateur.getEmail());
		
		
	}
	
	public void debitToBankAccount(BigDecimal amount, Utilisateur utilisateur) {
		logger.info("debit bank account from "+utilisateur.getNom()+" of euros : "+amount);
		ComptePMB compte = getComptePMB(utilisateur);
		compte.setMontant(amount.subtract(compte.getMontant()));
		comptePMBRepository.save(compte);	
		
	}
	
	public void creditFromBankAccount(BigDecimal amount, Utilisateur utilisateur) {
		logger.info("credit bank account from "+utilisateur.getNom()+" of euros : "+amount);
		ComptePMB compte = getComptePMB(utilisateur);
		compte.setMontant(amount.add(compte.getMontant()));
		comptePMBRepository.save(compte);	
		
	}
	
	

}
