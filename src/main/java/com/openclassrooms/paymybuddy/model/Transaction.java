package com.openclassrooms.paymybuddy.model;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private int transactionId;
	
	@Column(name = "emetteur")
	private Utilisateur emetteur;
	
	@Column(name = "destinataire")
	private Utilisateur destinataire;
	
	@Column(name = "somme")
	private BigDecimal somme;
	
	@Column(name = "commission")
	private float commission;
	
	@Column(name = "horodatage")
	private Date horodatage;
	
	@Column(name = "status")
	private StatusTransaction status;
	
	@Column(name = "etat_facturation")
	private EtatFacturation etatFacturation;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public Utilisateur getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(Utilisateur emetteur) {
		this.emetteur = emetteur;
	}

	public Utilisateur getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(Utilisateur destinataire) {
		this.destinataire = destinataire;
	}

	public BigDecimal getSomme() {
		return somme;
	}

	public void setSomme(BigDecimal somme) {
		this.somme = somme;
	}

	public float getCommission() {
		return commission;
	}

	public void setCommission(float commission) {
		this.commission = commission;
	}

	public Date getHorodatage() {
		return horodatage;
	}

	public void setHorodatage(Date horodatage) {
		this.horodatage = horodatage;
	}

	public StatusTransaction getStatus() {
		return status;
	}

	public void setStatus(StatusTransaction status) {
		this.status = status;
	}

	public EtatFacturation getEtatFacturation() {
		return etatFacturation;
	}

	public void setEtatFacturation(EtatFacturation etatFacturation) {
		this.etatFacturation = etatFacturation;
	}

}
