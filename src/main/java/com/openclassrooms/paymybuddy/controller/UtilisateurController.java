package com.openclassrooms.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

import jakarta.validation.Valid;

@Controller
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;

	@GetMapping("/register")
	public String getRegister(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "register";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@PostMapping("/register/confirmRegister")
    public String confirmRegister(@ModelAttribute("utilisateur") @Valid Utilisateur utilisateur, BindingResult bindingResult, Model model) {
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
        return "register-success";
    }

}
