package com.openclassrooms.paymybuddy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.openclassrooms.paymybuddy.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	public Contact findById(int id);
	
	@Query(value = "SELECT * FROM contact WHERE utilisateur_id = :userId", nativeQuery = true)
    Page<Contact> findByUtilisateurId(int userId, Pageable pageable);
	
	@Query(value = "SELECT * FROM contact WHERE utilisateur_id = :userId", nativeQuery = true)
    List<Contact> findByUtilisateurId(int userId);

}
