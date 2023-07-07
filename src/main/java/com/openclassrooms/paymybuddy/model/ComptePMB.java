package com.openclassrooms.paymybuddy.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	public int getComptePMBId() {
		return comptePMBId;
	}

	public void setComptePMBId(int comptePMBId) {
		this.comptePMBId = comptePMBId;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

}
