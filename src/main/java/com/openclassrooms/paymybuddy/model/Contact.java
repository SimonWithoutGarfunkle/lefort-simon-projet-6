package com.openclassrooms.paymybuddy.model;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@DynamicUpdate
@Table(name = "contact")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "contact_id")
	private Integer contactId;
	
	@NotEmpty
	@Column(name = "email")
	private String email;
	
	@Column(name = "telephone")
	private String telephone;
	
	@Column(name = "nom")
	private String nom;
	
	@Column(name = "prenom")
	private String prenom;
	
	@Column(name = "surnom")
	private String surnom;
	
	@Column(name = "commentaire")
	private String commentaire;

	public Contact(String nom, String email) {
		this.nom = nom;
		this.email = email;
	}


	public Contact() {
		super();
	}


	public Contact(Integer contactId, @NotEmpty String email, String telephone, String nom, String prenom,
			String surnom, String commentaire) {
		super();
		this.contactId = contactId;
		this.email = email;
		this.telephone = telephone;
		this.nom = nom;
		this.prenom = prenom;
		this.surnom = surnom;
		this.commentaire = commentaire;
	}


	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}


	public void setUtilisateurId(Integer utilisateurId) {
		this.contactId = utilisateurId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	public String getSurnom() {
		return surnom;
	}

	public void setSurnom(String surnom) {
		this.surnom = surnom;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
