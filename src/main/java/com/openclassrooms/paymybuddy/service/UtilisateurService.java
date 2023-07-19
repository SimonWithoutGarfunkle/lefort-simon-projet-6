package com.openclassrooms.paymybuddy.service;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.paymybuddy.model.ComptePMB;
import com.openclassrooms.paymybuddy.model.RIB;
import com.openclassrooms.paymybuddy.model.RoleUtilisateur;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	private static Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

	/**
	 * Retourne tous les utilisateurs enregistrés pour des tests en phase de
	 * developpement
	 * 
	 * @return tous les utilisateurs enregistrés dans la base
	 */
	public Iterable<Utilisateur> getAllUtilisateurs() {
		logger.info("Getting all users from DB");
		return utilisateurRepository.findAll();
	}

	/**
	 * Verifie si une adresse email est déja enregistrée dans la base
	 * 
	 * @param email
	 * @return true si l'email correspond deja a un compte utilisateur
	 */
	public Utilisateur getUtilisateurByEmail(String email) {
		logger.info("Recherche du compte "+ email);
		if (utilisateurRepository.findByEmail(email) == null) {
			logger.error("Aucun compte ne correspond a "+ email);
		}
		return utilisateurRepository.findByEmail(email);
	}

	/**
	 * Créer un compte pmb et un RIB associés a ce nouvel utilisateur et persiste
	 * l'ensemble en base Il ne peut y avoir qu'un seul utilisateur par adresse
	 * email.
	 * 
	 * @param utilisateur a enregistrer en base
	 * @return l'utilisateur enregistre
	 */
	public Utilisateur addUtilisateur(Utilisateur utilisateur) {
		if (!(getUtilisateurByEmail(utilisateur.getEmail()) == null)) {
			String errorMessage = "Cet e-mail est déjà enregistré";
			logger.error(errorMessage);
			throw new IllegalArgumentException(errorMessage);

		}
		logger.info("Creation de l'utilisateur");
		ComptePMB comptepmb = new ComptePMB();
		RIB rib = new RIB();
		BigDecimal initialMontant = new BigDecimal(0);
		comptepmb.setMontant(initialMontant);
		utilisateur.setComptePMB(comptepmb);
		utilisateur.setRib(rib);
		utilisateur.setRole(RoleUtilisateur.USER);
		return utilisateurRepository.save(utilisateur);
	}
	
	/**
	 * Verifie que l'email et le mot de passe correspondent bien à un utilisateur de la base
	 * 
	 * @param email
	 * @param motDePasse
	 * @return true si l'email et le mot de passe correspondent bien à un utilisateur
	 */
	/*public boolean loginUtilisateur(String email, String motDePasse) {
		Iterable<Utilisateur> utilisateurs = utilisateurRepository.findByEmail(email);
		Utilisateur premierUtilisateur = new Utilisateur();
		for (Utilisateur utilisateur : utilisateurs) {
		    premierUtilisateur = utilisateur;
		    break;
		}
	    if (premierUtilisateur != null) {	     
	        if ((premierUtilisateur.getEmail().equals(email))&&(premierUtilisateur.getMotDePasse().equals(motDePasse))) {
	            return true; 
	        }
	    }
	    return false;
    }*/

}
