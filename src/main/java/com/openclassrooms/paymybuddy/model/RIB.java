package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rib")
public class RIB {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rib_id")
	private Integer ribId;
	
	@Column(name = "iban")
	private String IBAN;
	
	@Column(name = "bic")
	private String BIC;

	public Integer getRibId() {
		return ribId;
	}

	public void setRibId(Integer ribId) {
		this.ribId = ribId;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public String getBIC() {
		return BIC;
	}

	public void setBIC(String bIC) {
		BIC = bIC;
	}

}
