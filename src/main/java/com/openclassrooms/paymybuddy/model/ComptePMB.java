package com.openclassrooms.paymybuddy.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "comptepmb")
public class ComptePMB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comptepmb_id")
	private int comptePMBId;
	
	@Column(name = "montant")
	private BigDecimal montant;
	
	@OneToMany(mappedBy = "emetteur")
	@JsonManagedReference
    private List<Transaction> transactionsEmises;

    @OneToMany(mappedBy = "destinataire")
    @JsonManagedReference
    private List<Transaction> transactionsRecues;

	public int getComptePMBId() {
		return comptePMBId;
	}

	public void setComptePMBId(int comptePMBId) {
		this.comptePMBId = comptePMBId;
	}

	public List<Transaction> getTransactionsEmises() {
		return transactionsEmises;
	}

	public void setTransactionsEmises(List<Transaction> transactionsEmises) {
		this.transactionsEmises = transactionsEmises;
	}

	public List<Transaction> getTransactionsRecues() {
		return transactionsRecues;
	}

	public void setTransactionsRecues(List<Transaction> transactionsRecues) {
		this.transactionsRecues = transactionsRecues;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

}
