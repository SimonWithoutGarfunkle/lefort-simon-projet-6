package com.openclassrooms.paymybuddy.service;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import com.openclassrooms.paymybuddy.model.ComptePMB;
import com.openclassrooms.paymybuddy.model.RIB;
import com.openclassrooms.paymybuddy.model.RoleUtilisateur;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;

@Service
public class UtilisateurService implements UserDetailsService {

	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private static Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

	/**
	 * Retourne tous les utilisateurs enregistrés pour des tests en phase de
	 * developpement
	 * 
	 * @return tous les utilisateurs enregistrés dans la base
	 */
	public Iterable<Utilisateur> getAllUtilisateurs() {
		logger.info("Recuperation de tous les utilisateurs de la base");
		return utilisateurRepository.findAll();
	}

	/**
	 * Verifie si une adresse email est déja enregistrée dans la base
	 * 
	 * @param email
	 * @return true si l'email correspond deja a un compte utilisateur
	 */
	public Utilisateur getUtilisateurByEmail(String email) {
		logger.info("Recherche du compte " + email);
		if (utilisateurRepository.findByEmail(email) == null) {
			logger.error("Aucun compte ne correspond a " + email);
		}
		return utilisateurRepository.findByEmail(email);
	}

	/**
	 * Met à jour dynamiquement l'utilisateur en base
	 * 
	 * @param utilisateur
	 * @return l'utilisateur mis a jour
	 */
	public Utilisateur updateUtilisateur(Utilisateur utilisateur) {
		logger.info("mise a jour de l'utilisateur");
		return utilisateurRepository.save(utilisateur);
	}
	
	/**
	 * Met à jour les champs accessible du profil de l'utilisateur
	 * 
	 * @param utilisateur
	 * @return l'utilisateur mis a jour
	 */
	public Utilisateur updateProfilUtilisateur(Utilisateur utilisateur) {
		logger.info("mise a jour du profil");
		Utilisateur utilisateurInBase = utilisateurRepository.findByEmail(utilisateur.getEmail());
		utilisateurInBase.setNom(utilisateur.getNom());
		utilisateurInBase.setPrenom(utilisateur.getPrenom());
		return utilisateurRepository.save(utilisateurInBase);
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
		utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
		utilisateur.setComptePMB(comptepmb);
		utilisateur.setRib(rib);
		utilisateur.setRole(RoleUtilisateur.USER);
		return utilisateurRepository.save(utilisateur);
	}
	
	/**
	 * Génère un utilisateur pré rempli avec les informations récupérées de la connexion via Google
	 * 
	 * @returnutilisateur pré rempli avec les informations récupérées de la connexion via Google
	 */
	public Utilisateur firstSocialLoginUtilisateur() {
		logger.info("appel de firstSocialLoginUtilisateur");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
		String email = (String) oauth2User.getAttributes().get("email");
		if (!(utilisateurRepository.findByEmail(email)==null)) {
			return utilisateurRepository.findByEmail(email);
			
		}
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setEmail(email);
		utilisateur.setNom((String) oauth2User.getAttributes().get("family_name"));
		utilisateur.setPrenom((String) oauth2User.getAttributes().get("given_name"));
		
		
		return utilisateur;
	
	}

	/**
	 * Definie l'adresse email de l'utilisateur comme identifiant de connexion
	 * 
	 */	 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("appel de loadUserByUsername");
		Utilisateur user = utilisateurRepository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("Aucun utilisateur ne correspond a cet identifiant");
		}

		return user;
	}


}
