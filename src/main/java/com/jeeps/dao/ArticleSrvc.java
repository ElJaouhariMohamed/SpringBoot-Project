package com.jeeps.dao;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import com.jeeps.Entities.Article;
import com.jeeps.Entities.Ecole;
import com.jeeps.Entities.User;

public interface ArticleSrvc {
	public List<Article> allArticlesDirect();
	public boolean ajouterArticleEcole(long id_school,String title,MultipartFile image, String message, long user_id,String cle) throws IOException;
	public boolean ajouterArticleCategory(long category_id,String title,MultipartFile image, String message,long user_id,String cle) throws IOException;
	public boolean modifierArticle(Article article_,long id_school,MultipartFile img,String title,String message,long user_id,String cle);
	public boolean supprimerArticle(Article a);
	public List<Article> allArticles(Ecole ecole);
	public Article getArticle(Long id_article);
	public List<Article> AllArticlesByUser(User user);
	public List<Article> AllArticlesByclé(String cle, long id_category,String related,User user);
	public List<Article> findArticlesByUser(String Username);
}
