package com.openclassrooms.paymybuddy.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "transaction_id")
	private int transactionId;

    @ManyToOne
    @JoinColumn(name = "emetteur_id")
    @JsonBackReference
    private ComptePMB emetteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    @JsonBackReference
    private ComptePMB destinataire;
		
    @Column(name = "somme")
	private BigDecimal somme;
	
	@Column(name = "commission")
	private float commission;
	
	@Column(name = "montant_commission")
	private BigDecimal montantCommission;
	
	public BigDecimal getMontantCommission() {
		return montantCommission;
	}

	public void setMontantCommission(BigDecimal montantCommission) {
		this.montantCommission = montantCommission;
	}

	@Column(name = "horodatage")
	private Date horodatage;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private StatusTransaction status;
	
	@Column(name = "etat_facturation")
	@Enumerated(EnumType.STRING)
	private EtatFacturation etatFacturation;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public ComptePMB getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(ComptePMB emetteur) {
		this.emetteur = emetteur;
	}

	public ComptePMB getDestinataire() {
		return destinataire;
	}

	public void setDestinataire(ComptePMB destinataire) {
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
