package com.jeeps.Entities;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Contact")
public class Contact implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long contact_id;
	private String nom;
	private String numTel;
	private String adresse;
	private String email;
	
	@ManyToOne
	@JoinColumn(name="ecole_id")
	private Ecole ecole;

	

	public long getContact_id() {
		return contact_id;
	}

	public void setContact_id(long contact_id) {
		this.contact_id = contact_id;
	}

	public String getNumTel() {
		return numTel;
	}

	public void setNumTel(String numTel) {
		this.numTel = numTel;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Ecole getEcole() {
		return ecole;
	}

	public void setEcole(Ecole ecole) {
		this.ecole = ecole;
	}

	public Contact(String numTel, String adresse, String email, Ecole ecole) {
		super();
		this.numTel = numTel;
		this.adresse = adresse;
		this.email = email;
		this.ecole = ecole;
	}
	
	public Contact() {}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
}
