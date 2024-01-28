package com.jeeps.Entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Categorie")
public class Categorie implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String nom;
	private String date;
	@Column(columnDefinition = "MEDIUMBLOB")
	private String img;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@OneToMany(mappedBy="categorie")
	private List<Article> articles_c;

	public long getId() {
		return id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Article> getArticles_c() {
		return articles_c;
	}

	public void setArticles_c(List<Article> articles_c) {
		this.articles_c = articles_c;
	}

	public Categorie(String nom, List<Article> articles) {
		super();
		this.nom = nom;
		this.articles_c = articles;
	}
	
	public Categorie() {}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
