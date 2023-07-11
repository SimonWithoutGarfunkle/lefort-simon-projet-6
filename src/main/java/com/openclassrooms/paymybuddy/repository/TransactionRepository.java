package com.openclassrooms.paymybuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.paymybuddy.model.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}
