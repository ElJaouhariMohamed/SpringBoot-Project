package com.jeeps.Entities;

import java.io.Serializable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Formation")
public class Formation implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long formation_id;
	private String nom;
	private String duree;
	private String niveau;
	private String Frais;
	
	@ManyToOne
	@JoinColumn(name="ecole_id")
	private Ecole ecole;

	

	public long getFormation_id() {
		return formation_id;
	}

	public void setFormation_id(long formation_id) {
		this.formation_id = formation_id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDuree() {
		return duree;
	}

	public void setDuree(String duree) {
		this.duree = duree;
	}

	public String getNiveau() {
		return niveau;
	}

	public void setNiveau(String niveau) {
		this.niveau = niveau;
	}

	public String getFrais() {
		return Frais;
	}

	public void setFrais(String frais) {
		Frais = frais;
	}

	public Ecole getEcole() {
		return ecole;
	}

	public void setEcole(Ecole ecole) {
		this.ecole = ecole;
	}
	
	public Formation() {}

	public Formation(String nom, String duree, String niveau, String frais, Ecole ecole) {
		super();
		this.nom = nom;
		this.duree = duree;
		this.niveau = niveau;
		this.Frais = frais;
		this.ecole = ecole;
	}
	
	
}
