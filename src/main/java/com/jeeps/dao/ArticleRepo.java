package com.jeeps.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jeeps.Entities.Article;
import com.jeeps.Entities.Categorie;
import com.jeeps.Entities.Ecole;
import com.jeeps.Entities.User;

public interface ArticleRepo extends JpaRepository<Article,Long>{
	List<Article> findByEcole(Ecole ecole);
	List<Article> findByUser(User user);
	List<Article> findByCategorie(Categorie categorie);
	List<Article> findByCle(String cle);
}
