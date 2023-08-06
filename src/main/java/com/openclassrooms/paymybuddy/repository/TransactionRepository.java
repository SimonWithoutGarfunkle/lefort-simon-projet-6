package com.openclassrooms.paymybuddy.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.openclassrooms.paymybuddy.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	default Transaction getTransactionById(Integer id) {
		return findById(id).orElse(null);
	}
	
	 @Query(value = "SELECT * FROM contact WHERE utilisateur_id = :userId", nativeQuery = true)	 
	 Page<Transaction> findByUtilisateurId(Integer userId, Pageable pageable);
	 
	 @Query(value = "SELECT * FROM contact WHERE utilisateur_id = :userId", nativeQuery = true)	 
	 List<Transaction> findByUtilisateurId(Integer userId);

}
