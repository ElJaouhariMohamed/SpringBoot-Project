package com.jeeps.Entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Image")
public class Image implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long image_id;
	private String Titre;
	private String Description;
	@Column(length=9000,unique=true)
	private String URL;
	private String Date ;
	
	@ManyToMany
	private List<Article> articles;
	
	@OneToOne(mappedBy="profileimg")
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="ecole_id")
	private Ecole ecole;

	
	public long getImage_id() {
		return image_id;
	}

	public void setImage_id(long image_id) {
		this.image_id = image_id;
	}


	public String getTitre() {
		return Titre;
	}

	public void setTitre(String titre) {
		this.Titre = titre;
	}

	public String getDescription() {
		return this.Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public String getURL() {
		return this.URL;
	}

	public void setURL(String uRL) {
		this.URL = uRL;
	}

	

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Ecole getEcole() {
		return ecole;
	}

	public void setEcole(Ecole ecole) {
		this.ecole = ecole;
	}
	
	public Image() {}

	public Image(String titre, String description, String uRL, String Date, User user) {
		super();
		this.Titre = titre;
		this.Description = description;
		this.URL = uRL;
		this.Date = Date;
		this.user = user;
	}
	
	
}
