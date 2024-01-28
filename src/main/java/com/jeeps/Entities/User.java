package com.jeeps.Entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Users")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long user_id;
	private String username;
	private String userpass;
	private String nom;
	private String prenom;
	private String Informations;
	private String role;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="image_id")
	private Image profileimg;
	
	@OneToMany(mappedBy="user",cascade = CascadeType.ALL)
	private List<Article> articles;
	
	@ManyToOne
	@JoinColumn(name="ecole_id")
	private Ecole ecole;

	public String getInformations() {
		return Informations;
	}

	public void setInformations(String informations) {
		Informations = informations;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpass() {
		return userpass;
	}

	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}

	public Image getProfileimg() {
		return profileimg;
	}

	public void setProfileimg(Image profileimg) {
		this.profileimg = profileimg;
	}
	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
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


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Ecole getEcole() {
		return ecole;
	}

	public void setEcole(Ecole ecole) {
		this.ecole = ecole;
	}
	
	public int getSize() {
		if (Objects.nonNull(this.articles)) {
			return this.getArticles().size();
		} else {
			return 0;
		}
	}
	
	public User() {}

	public User(String username, String userpass, Image profileimg) {
		super();
		this.username = username;
		this.userpass = userpass;
		this.profileimg = profileimg;

	}
		
}
