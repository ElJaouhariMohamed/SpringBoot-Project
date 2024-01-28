package com.jeeps.dao;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jeeps.Entities.Article;
import com.jeeps.Entities.Categorie;
import com.jeeps.Entities.User;

@Service
@Transactional
public class CategorieSrvcImpl implements CategorieSrvc {
	
	@Autowired
	CategorieRepo cr;
	
	@Autowired
	ArticleRepo ar;
	
	@Autowired
	UserRepo ur;
	@Override
	public List<Categorie> allCategorie() {
		
		return cr.findAll();
	}
	
	

	@Override
	public List<Article> allArticlesbyCategorie(Categorie categorie) {
		return ar.findByCategorie(categorie);
	}



	@Override
	public Optional<Categorie> CategorieByID(long category_id) {

		return  cr.findById(category_id);
	}

	@Override
	public boolean deleteCat(Categorie category) {
		try {
		cr.delete(category);
		return true;
	}catch(Exception e) {
		return false;
	}

	}

	@Override
	public boolean modifierArticle(Article article_,long category_id,MultipartFile img, String title,String message,long user_id,String cle) {
		LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String dateString = currentDate.format(dateFormat);
		
	    User user = ur.findById(user_id).get();
		article_.setTitre(title);
		article_.setText(message);
		article_.setDate(dateString);
		article_.setUser(user);
		article_.setCategorie(cr.findById(category_id).get());
		article_.setCle(cle);
		try {
			article_.setImg(Base64.getEncoder().encodeToString(img.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Article result = ar.save(article_);
		if (Objects.nonNull(result)) {
			return true;
		}
		else {
			return false;
		}
	}



	public boolean ajouterCategory(String title, MultipartFile images, long user_id) throws IOException {
		
		LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String dateString = currentDate.format(dateFormat);
	    
	    User user = ur.findById(user_id).get();
	    
	    Categorie cat = new Categorie();
	    cat.setUser(user);
	    cat.setNom(title);
	    cat.setDate(dateString);
	    cat.setImg(Base64.getEncoder().encodeToString(images.getBytes()));
		
	    Categorie cat_ = cr.save(cat);
	    
	    if (Objects.nonNull(cat_)) {
			return true;
		}
		else {
			return false;
		}
	    
	}



	public boolean modifierCat(String title, MultipartFile images, long user_id, long id) throws IOException {
		
		LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String dateString = currentDate.format(dateFormat);
	    
	    User user = ur.findById(user_id).get();
	    Optional<Categorie> cat_ = cr.findById(id);
	    Categorie cat__ = cat_.get();
	    cat__.setUser(user);
	    cat__.setNom(title);
	    cat__.setDate(dateString);
	    cat__.setImg(Base64.getEncoder().encodeToString(images.getBytes()));
		
	    Categorie result = cr.save(cat__);
	    
	    if (Objects.nonNull(result)) {
			return true;
		}
		else {
			return false;
		}
		
	}
}
