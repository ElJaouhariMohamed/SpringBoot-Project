package com.jeeps.Entities;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="Article")
public class Article implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long article_id;
	private String Titre;
	@Column(length = 3000000)
	private String Text;
	private boolean valide;
	private String date;
	@Column(name = "clé")
	private String cle;
	
	@Column(columnDefinition = "MEDIUMBLOB")
	private String img;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	private Categorie categorie;
	
	@ManyToOne
	private Ecole ecole;

	

	public long getArticle_id() {
		return article_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Ecole getEcole() {
		return ecole;
	}

	public void setEcole(Ecole ecole) {
		this.ecole = ecole;
	}

	public void setArticle_id(long article_id) {
		this.article_id = article_id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitre() {
		return Titre;
	}

	public void setTitre(String titre) {
		Titre = titre;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	public boolean isValide() {
		return valide;
	}

	public void setValide(boolean valide) {
		this.valide = valide;
	}
/*
	public List<Image> getImage() {
		return image;
	}

	public void setImage(List<Image> image) {
		this.image = image;
	}
*/
	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}

	public String getUsername() {
		return user.getUsername();
	}
	
	public String getInformations() {
		return user.getInformations();
	}
	
	public long getEcole_id() {
		return ecole.getEcole_id();
	}
	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		this.cle = cle;
	}
	
	public Article(String titre, String text, boolean valide, Categorie categorie,
			String clé,String img) {
		super();
		Titre = titre;
		Text = text;
		this.valide = valide;
		//this.image = image;
		this.categorie = categorie;
		this.cle = clé;
		this.img = img;
		
	}
	public Article() {}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
