package com.openclassrooms.paymybuddy.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

@Entity
@DynamicUpdate
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {
	
	private static final long serialVersionUID = 1L;

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
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "utilisateur_id")
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
	

	public Utilisateur(int utilisateurId, @NotEmpty String email, @NotEmpty String motDePasse, @NotEmpty String nom,
			@NotEmpty String prenom, List<Contact> contacts, RIB rib, ComptePMB comptePMB, RoleUtilisateur role) {
		super();
		this.utilisateurId = utilisateurId;
		this.email = email;
		this.motDePasse = motDePasse;
		this.nom = nom;
		this.prenom = prenom;
		this.contacts = contacts;
		this.rib = rib;
		this.comptePMB = comptePMB;
		this.role = role;
	}
	
	

	public Utilisateur() {
		super();
	}



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
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(this.getRole().toString());
        return Arrays.asList(authority);
    }
 
    @Override
    public String getPassword() {
        return this.getMotDePasse();
    }
 
    @Override
    public String getUsername() {
        return this.getEmail();
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
    @Override
    public boolean isEnabled() {
        return true;
    }

}
