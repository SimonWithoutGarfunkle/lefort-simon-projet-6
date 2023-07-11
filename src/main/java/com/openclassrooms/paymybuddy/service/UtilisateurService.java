package com.openclassrooms.paymybuddy.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.ComptePMB;
import com.openclassrooms.paymybuddy.model.RIB;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;

@Service
public class UtilisateurService {
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	private static Logger logger = LoggerFactory.getLogger(UtilisateurService.class);
	
	public Iterable<Utilisateur> getAllUtilisateurs() {
		return utilisateurRepository.findAll();
	}
	
	public Utilisateur addUtilisateur(Utilisateur utilisateur) {
		logger.info("Ajout d'un nouvel utilisateur");
		ComptePMB comptepmb = new ComptePMB();
		RIB rib = new RIB();
		BigDecimal initialMontant = new BigDecimal(0);
		comptepmb.setMontant(initialMontant);
		utilisateur.setComptePMB(comptepmb);
		utilisateur.setRib(rib);
		return utilisateurRepository.save(utilisateur);
	}

}
