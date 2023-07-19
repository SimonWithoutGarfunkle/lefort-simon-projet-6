package com.openclassrooms.paymybuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

	
	
	@GetMapping("/dashboard")
	public String getDashboard() {
		logger.info("performing get dashboard");
		return "dashboard";
	}
	
    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }
	
	
	@GetMapping("/login")
	public String getLogin() {
		logger.info("performing get login");
		return "login";
	}
	
	/*
	@PostMapping("/login")
    public String processLogin(@RequestParam("email") String email, @RequestParam("motdepasse") String motDePasse, Model model) {
		System.out.println("coucou" + email);
		logger.info("Post de login");
		
		String email = utilisateur.getEmail();
		String motDePasse = utilisateur.getMotDePasse();
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, motDePasse);
	    Authentication authentication = authenticationManager.authenticate(authRequest);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/dashboard";
    }*/

	

	
	@GetMapping("/register")
	public String getRegister(Model model) {
		logger.info("performing get register");
		model.addAttribute("utilisateur", new Utilisateur());
		return "register";
	}
	
	@PostMapping("/register/confirmRegister")
    public String confirmRegister(@ModelAttribute("utilisateur") @Valid Utilisateur utilisateur, BindingResult bindingResult, Model model) {
		logger.info("performing post register");
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
