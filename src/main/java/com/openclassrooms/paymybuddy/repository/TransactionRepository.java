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
	
	List<Transaction> findByEmetteurComptePMBId(Integer id);
	
	List<Transaction> findByDestinataireComptePMBId(Integer id);
	
	@Query("SELECT t FROM Transaction t WHERE t.destinataire.comptePMBId = :id")
	Page<Transaction> findByDestinataireComptePMBId(Integer id, Pageable pageable);
	
	@Query("SELECT t FROM Transaction t WHERE t.emetteur.comptePMBId = :id")
	Page<Transaction> findByEmetteurComptePMBId(Integer id, Pageable pageable);


}
