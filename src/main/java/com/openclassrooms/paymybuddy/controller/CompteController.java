package com.openclassrooms.paymybuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class CompteController {
	
	private static Logger logger = LoggerFactory.getLogger(CompteController.class);
	
	@GetMapping("/transactions")
	public String getTransactions() {
		logger.info("performing get transactions");
		return "transactions";
	}

}
