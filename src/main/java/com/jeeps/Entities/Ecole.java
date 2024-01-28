package com.jeeps.Entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name="Ecole")
public class Ecole implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long ecole_id;
	private String Nom;
	private String Niveau;
	private int anncreation;
	private int nbrEtd;
	private int nbrStaff;
	private int nbrCollaborations;
	
	@OneToMany(mappedBy="ecole",fetch = FetchType.EAGER)
	private List<Image> images;
	
	@OneToMany(mappedBy="ecole")
	private List<Article> articles;
	
	@OneToMany(mappedBy="ecole")
	private List<Formation> formations;
	
	@OneToMany(mappedBy="ecole")
	private List<Contact> contacts;
	
	@OneToMany(mappedBy="ecole")
	private List<User> participants;

	

	public long getEcole_id() {
		return ecole_id;
	}

	public void setEcole_id(long ecole_id) {
		this.ecole_id = ecole_id;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getNiveau() {
		return Niveau;
	}

	public void setNiveau(String niveau) {
		Niveau = niveau;
	}

	public int getAnncreation() {
		return anncreation;
	}

	public void setAnncreation(int anncreation) {
		this.anncreation = anncreation;
	}

	public int getNbrEtd() {
		return nbrEtd;
	}

	public void setNbrEtd(int nbrEtd) {
		this.nbrEtd = nbrEtd;
	}

	public int getNbrStaff() {
		return nbrStaff;
	}

	public void setNbrStaff(int nbrStaff) {
		this.nbrStaff = nbrStaff;
	}	

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public List<Formation> getFormations() {
		return formations;
	}

	public void setFormations(List<Formation> formations) {
		this.formations = formations;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	
	public int getNbrCollaborations() {
		return nbrCollaborations;
	}

	public void setNbrCollaborations(int nbrCollaborations) {
		this.nbrCollaborations = nbrCollaborations;
	}
	
	public int getSizeArticles() {
		if (Objects.nonNull(this.articles)) {
			return this.getArticles().size();
		} else {
			return 0;
		}
	}
	
	public int getSizeContact() {
		if (Objects.nonNull(this.contacts)) {
			return this.getContacts().size();
		} else {
			return 0;
		}
	}

	public int getSizeFormations() {
		if (Objects.nonNull(this.formations)) {
			return this.getFormations().size();
		} else {
			return 0;
		}
	}
	
	public Ecole() {}

	public Ecole(String nom, String niveau, int anncreation, int nbrEtd, int nbrStaff,int nbrCollabs, List<Image> images,
			List<Article> articles, List<Formation> formations, List<Contact> contacts) {
		super();
		Nom = nom;
		Niveau = niveau;
		this.nbrCollaborations=nbrCollabs;
		this.anncreation = anncreation;
		this.nbrEtd = nbrEtd;
		this.nbrStaff = nbrStaff;
		this.images = images;
		this.articles = articles;
		this.formations = formations;
		this.contacts = contacts;
	}

	
	
	
	
}
