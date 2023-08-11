package com.openclassrooms.paymybuddy.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

import jakarta.validation.Valid;

@Controller
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;
	
	@Autowired
	private TransactionService transactionService;

	private static Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

	@GetMapping("/dashboard")
	public String getDashboard() {
		logger.info("appel get dashboard");
		return "dashboard";
	}

	@GetMapping("/403")
	public String error403() {
		logger.info("appel get error 403");
		return "/error/403";
	}

	@GetMapping("/login")
	public String getLogin() {
		logger.info("appel get login");
		return "login";
	}

	@GetMapping("/profil")
	public String getProfil(Model model) {
		logger.info("appel get profil");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
		Utilisateur utilisateurAJour = utilisateurService.getUtilisateurByEmail(utilisateur.getEmail());
		model.addAttribute("utilisateur", utilisateurAJour);
		
		if (model.containsAttribute("infoMessage")) {
		        model.addAttribute("infoMessage", model.getAttribute("infoMessage"));
		    }
		    if (model.containsAttribute("errorMessage")) {
		        model.addAttribute("errorMessage", model.getAttribute("errorMessage"));
		    }		

		return "profil";
	}

	@PostMapping("/profil")
	public String updateProfile(@ModelAttribute("utilisateur") Utilisateur utilisateur, Model model,
			BindingResult bindingResult) {
		logger.info("appel update profil");
		Utilisateur utilisateurAJour = new Utilisateur();
		utilisateurAJour = utilisateurService.updateProfilUtilisateur(utilisateur);
		model.addAttribute("utilisateur", utilisateurAJour);
		String infoMessage = "Modification enregistrée";
		model.addAttribute("infoMessage", infoMessage);
		return "profil";

	}

	@Transactional
	@PostMapping("/profil/banking")
	public String bankingOperation(@RequestParam BigDecimal montant, @RequestParam String action, Model model) {
		logger.info("appel post bank : "+action);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
		boolean result = true;
		 if ("debit".equals(action)) {
			 result = transactionService.debitToBankAccount(montant, utilisateur);
		} else if ("credit".equals(action)) {
			result = transactionService.creditFromBankAccount(montant, utilisateur);
		}
		 		 
		if (result) {
			String Message = "Opération confirmée";
			model.addAttribute("infoMessage", Message);			
		} else {
			String Message = "Montant disponible insuffisant pour confirmer l'opération";
			model.addAttribute("errorMessage", Message);	
		}

		Utilisateur utilisateurAJour = utilisateurService.getUtilisateurByEmail(utilisateur.getEmail());
		model.addAttribute("utilisateur", utilisateurAJour);
		return "profil";

	}

	@GetMapping("/register")
	public String getRegister(Model model) {
		logger.info("appel get register");
		model.addAttribute("utilisateur", new Utilisateur());
		return "register";
	}
	
	@GetMapping("/register/social")
	public String getRegisterSocial(Model model) {
		logger.info("appel get register");
		Utilisateur utilisateur = utilisateurService.firstSocialLoginUtilisateur();
		model.addAttribute("utilisateur", utilisateur);
		return "register";
	}
	
	@GetMapping("/login/oauth2/code/facebook")
	public String getRegisterFacebook(Model model) {
		logger.info("appel get register facebook");
		Utilisateur utilisateur = utilisateurService.firstSocialLoginUtilisateur();
		model.addAttribute("utilisateur", utilisateur);
		return "register";
	}

	@Transactional
	@PostMapping("/register/confirmRegister")
	public String confirmRegister(@ModelAttribute("utilisateur") @Valid Utilisateur utilisateur,
			BindingResult bindingResult, Model model) {
		logger.info("appel post register");
		try {
			utilisateurService.addUtilisateur(utilisateur);
		} catch (IllegalArgumentException e) {
			bindingResult.rejectValue("email", "utilisateur.email", e.getMessage());
			model.addAttribute("utilisateur", utilisateur);
			model.addAttribute("errorMessage", e.getMessage());
			return "register";
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("utilisateur", utilisateur);
			return "register";
		}

		return "redirect:/register/success";
	}

	@GetMapping("/register/success")
	public String registerSuccess() {
		return "registerSuccess";
	}

}
