package com.jeeps.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.jeeps.Entities.Article;
import com.jeeps.Entities.Categorie;

public interface CategorieSrvc {
	public List<Categorie> allCategorie();
	public List<Article> allArticlesbyCategorie(Categorie categorie); 
	public Optional<Categorie> CategorieByID(long category_id);
	public boolean deleteCat(Categorie category);
	public boolean modifierArticle(Article article,long category_id,MultipartFile img,String title,String message,long user_id,String cle);
}
