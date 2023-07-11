package com.openclassrooms.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.UtilisateurService;


@Controller
public class UtilisateurController {
	
	@Autowired
	private UtilisateurService utilisateurService;

	
	
	@GetMapping("/register")
	public String getRegister(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "register";
	}	
	
	@RequestMapping(value = "/register/confirmRegister", method = RequestMethod.POST)
    public String confirmRegister(@ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult errors, Model model) {
		utilisateurService.addUtilisateur(utilisateur);
		return "register";
    }
	
	

}
