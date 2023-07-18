package com.openclassrooms.paymybuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.service.UtilisateurService;

import jakarta.validation.Valid;

@Controller
public class UtilisateurController {

	@Autowired
	private UtilisateurService utilisateurService;

	
	
	@GetMapping("/dashboard")
	public String getDashboard() {
		return "dashboard";
	}
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	@PostMapping("/login")
    public String authentifierUtilisateur(@RequestParam("email") String email, @RequestParam("motdepasse") String motDePasse, Model model) {
        if (utilisateurService.loginUtilisateur(email, motDePasse)) {          
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Identifiants invalides");
            return "login";
        }
    }
	
	@GetMapping("/register")
	public String getRegister(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		return "register";
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
