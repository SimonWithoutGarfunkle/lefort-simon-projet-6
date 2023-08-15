package com.openclassrooms.paymybuddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.openclassrooms.paymybuddy.model.ComptePMB;

@Repository
public interface ComptePMBRepository extends JpaRepository<ComptePMB, Integer> {
	
	default ComptePMB getComptePMBById(Integer id) {
		return findById(id).orElse(null);		
	}
	
	@Query("SELECT c FROM Utilisateur u JOIN u.comptePMB c WHERE u.utilisateurId = ?1")
	public ComptePMB findByUtilisateurId(Integer id);
	
	@Query("SELECT c FROM Utilisateur u JOIN u.comptePMB c WHERE u.email = ?1")
	public ComptePMB findByUtilisateurEmail(String email);
	

}
