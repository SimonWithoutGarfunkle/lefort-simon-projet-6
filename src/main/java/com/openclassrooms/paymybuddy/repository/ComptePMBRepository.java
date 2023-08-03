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
	
	@Query(value = "SELECT c FROM utilisateur u JOIN comptePMB c ON u.comptePMB_id = c.comptePMB_id WHERE u.utilisateur_id = ?1", nativeQuery = true)
	public ComptePMB findByUtilisateurId(Integer id);
	
	@Query(value = "SELECT c FROM utilisateur u JOIN comptePMB c ON u.comptePMB_id = c.comptePMB_id WHERE u.email = ?1", nativeQuery = true)
	public ComptePMB findByUtilisateurEmail(String email);
	

}
