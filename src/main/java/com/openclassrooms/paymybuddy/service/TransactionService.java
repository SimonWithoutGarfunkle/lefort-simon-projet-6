package com.openclassrooms.paymybuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;

@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	private static Logger logger = LoggerFactory.getLogger(TransactionService.class);
	
	public Iterable<Transaction> getAllTransactions() {
		logger.info("Recuperation de toutes les transactions de la base");
		return transactionRepository.findAll();
	}

}
