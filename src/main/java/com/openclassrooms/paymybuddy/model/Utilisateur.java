package com.openclassrooms.paymybuddy.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "utilisateur_id")
	private int utilisateurId;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mot_de_passe")
	private String motDePasse;
	
	@Column(name = "nom")
	private String nom;
	
	@Column(name = "prenom")
	private String prenom;
	
	@OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
	private List<Contact> contacts;
	
	@OneToMany(mappedBy = "emetteur", cascade = CascadeType.ALL)
    private List<Transaction> transactionsEmises;

    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
    private List<Transaction> transactionsRecues;
	
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

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public int getUtilisateurId() {
		return utilisateurId;
	}

	public void setUtilisateurId(int utilisateurId) {
		this.utilisateurId = utilisateurId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	

}
