package com.openclassrooms.paymybuddy.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "utilisateur")
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "utilisateur_id")
	private int utilisateurId;
	
	@Column(name = "email", nullable = false)
	@NotEmpty
	private String email;
	
	@Column(name = "mot_de_passe", nullable = false)
	@NotEmpty
	private String motDePasse;
	
	@Column(name = "nom", nullable = false)
	@NotEmpty
	private String nom;
	
	@Column(name = "prenom", nullable = false)
	@NotEmpty
	private String prenom;
	
	@OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Contact> contacts;
	    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "rib_id")
    private RIB rib;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "comptePMB_id")
    private ComptePMB comptePMB;
    
    @Column(name = "role")
	@Enumerated(EnumType.STRING)
    private RoleUtilisateur role;
	

	public RoleUtilisateur getRole() {
		return role;
	}

	public void setRole(RoleUtilisateur role) {
		this.role = role;
	}

	public RIB getRib() {
		return rib;
	}

	public void setRib(RIB rib) {
		this.rib = rib;
	}

	public ComptePMB getComptePMB() {
		return comptePMB;
	}

	public void setComptePMB(ComptePMB comptePMB) {
		this.comptePMB = comptePMB;
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
