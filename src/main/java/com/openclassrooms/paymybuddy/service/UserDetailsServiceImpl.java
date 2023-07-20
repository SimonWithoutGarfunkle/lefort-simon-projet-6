package com.openclassrooms.paymybuddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.paymybuddy.model.MyUserDetails;
import com.openclassrooms.paymybuddy.model.Utilisateur;
import com.openclassrooms.paymybuddy.repository.UtilisateurRepository;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
    private UtilisateurRepository utilisateurRepository;
     
    /**
     * Definie l'adresse email de l'utilisateur comme identifiant de connexion
     * 
     */
	@Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Utilisateur user = utilisateurRepository.findByEmail(username);
         
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }
         
        return new MyUserDetails(user);
    }

}
