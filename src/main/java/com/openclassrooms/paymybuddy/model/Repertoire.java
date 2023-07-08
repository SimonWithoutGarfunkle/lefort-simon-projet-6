package com.openclassrooms.paymybuddy.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "repertoire")
public class Repertoire {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "repertoire_id")
	private int repertoireId;
	// test synchro
	
	@Column(name = "contacts")
	private List<Contact> contacts;

	public int getRepertoireId() {
		return repertoireId;
	}

	public void setRepertoireId(int repertoireId) {
		this.repertoireId = repertoireId;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	

}
