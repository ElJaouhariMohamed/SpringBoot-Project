package com.jeeps.dao;

import java.util.List;
import java.util.Objects;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.jeeps.Entities.Article;
import com.jeeps.Entities.Categorie;
import com.jeeps.Entities.Ecole;
import com.jeeps.Entities.User;

@Service
@Transactional
public class ArticleSrvcImpl implements ArticleSrvc {
	@Autowired
	private ArticleRepo ar;
	
	@Autowired
	private CategorieRepo cr;
	
	@Autowired
	private EcoleRepo er;

	@Autowired
	private UserRepo ur;
	
	public boolean addArticleDirect(Article a) {
		Article article =  ar.save(a);
		if (article!=null) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	@Override
	public boolean ajouterArticleEcole(long ecole_id,String title,MultipartFile img, String message, long user_id,String cle) throws IOException {
			
			LocalDate currentDate = LocalDate.now();
		    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    String dateString = currentDate.format(dateFormat);
		    User user = ur.findById(user_id).get();
		    Ecole ecole = er.findById(ecole_id).get();
		    
		    Article article = new Article();
		    article.setDate(dateString);
		    article.setText(message);
		    article.setTitre(title);
		    article.setEcole(ecole);
		    article.setUser(user);
		    article.setCle(cle);
		     try {
				article.setImg(Base64.getEncoder().encodeToString(img.getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		     Article result = ar.save(article);
			if (result.equals(article)) {
				return true;
			}
			else {
				return false;
			}
		
	}

	@Override
	public boolean modifierArticle(Article article_,long id_school,MultipartFile img,String title,String message,long user_id,String cle)  {
		LocalDate currentDate = LocalDate.now();
	    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String dateString = currentDate.format(dateFormat);
	    User user = ur.findById(user_id).get();
		article_.setTitre(title);
		article_.setText(message);
		article_.setDate(dateString);
		article_.setUser(user);
		article_.setCle(cle);
		Ecole ecole = er.findById(id_school).get();
		article_.setEcole(ecole);
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


	@Override
	public boolean supprimerArticle(Article a) {
		try {
			ar.delete(a);
			return true;
		}catch(Exception e) {
			System.out.println("can't delete article");
			return false;
		}
	}

	@Override
	public List<Article> allArticles(Ecole ecole) {
		try {
			List<Article> articles = ar.findByEcole(ecole);
		return articles;
		} catch(Exception e) {
			System.out.println("500 : can't access articles by school ID");
		}
		return null;
	}

	@Override
	public Article getArticle(Long id_article) {
	    try {
	        Optional<Article> articleOpt = ar.findById(id_article);
	        if (articleOpt.isPresent()) {
	            Article article = articleOpt.get();
	            return article;
	        } else {
	            System.out.println("Article not found");
	        }
	    } catch(Exception e) {
	        System.out.println("500 : can't access articles by school ID");
	    }
	    return null;
	}

	@Override
	public List<Article> AllArticlesByUser(User user) {
		
		try {
	        List<Article> articles = ar.findByUser(user);
	        /*for (Article a : articles) {
	        	if (user.getRole().equalsIgnoreCase("user")) {
	        		if(a.getC) {
	        			
	        		}
	        	}
	        }*/
	        
	        
	        return articles;

	    } catch(Exception e) {
	        System.out.println("500 : can't access articles by school ID");
	    }
	    return null;
	}

	@Override
	public List<Article> AllArticlesByclé(String cle,long id,String related,User user) {
		
		try {
			List<Article> articles = new ArrayList<Article>();
			List<Article> articles_temp = new ArrayList<Article>();
			if (cle.equalsIgnoreCase("")) {
				return null;
			}
			String[] strArray = cle.split(",");
			for (int i = 0; i < strArray.length; i++) {
			    strArray[i] = strArray[i].trim().toLowerCase();
			}
			for (String str:strArray) {
				
				List<Article> articles_ = ar.findAll();
				
				for (Article a : articles_) {
					
					String[] array_clés = a.getCle().split(",");
					
					for (int i = 0; i < array_clés.length; i++) {
						array_clés[i] = array_clés[i].trim().toLowerCase();
					}
					
					
					boolean hasCle = Arrays.stream(array_clés)
		                     .map(str_ -> str_.split(","))
		                     .flatMap(Arrays::stream)
		                     .anyMatch(word -> word.equals(str));
						
					if (hasCle) {
							articles_temp.add(a);
						}
					}
				}
				
				if (user!=null && related.equalsIgnoreCase("user") ) 
					{
			
						for (Article art_:articles_temp) 
							{
							
								if (art_.getUser().getUsername().equalsIgnoreCase(user.getUsername())) 
								{
									articles.add(art_);
								
								}
							}
					}
				
				else if(related.equalsIgnoreCase("admin")) 
						{
							articles.addAll(articles_temp);
						} 
					else 
						{
							for (Article art:articles_temp) 
								{
									if (related.equals("categorie"))
										{
											if (art.getCategorie().getId()==id) 
												{
												articles.add(art);
												}
										}
									else 
										{
											if (art.getEcole().getEcole_id()==id) 
												{
													articles.add(art);
												}
										}
								}
						}
			
			Set<Article> setWithoutDuplicates = new HashSet<>(articles);
			List<Article> articles__ = new ArrayList<>(setWithoutDuplicates);
			return articles__;
			}
		catch(Exception e) {
			return null;
		}
	
	

	}


	@Override
	public boolean ajouterArticleCategory(long categorie_id, String title, MultipartFile img, String message,long user_id,String cle)
			throws IOException {
			LocalDate currentDate = LocalDate.now();
		    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    String dateString = currentDate.format(dateFormat);
		    User user = ur.findById(user_id).get();
		    Categorie cat = cr.findById(categorie_id).get();
		    
		    Article article = new Article();
		    article.setDate(dateString);
		    article.setText(message);
		    article.setTitre(title);
		    article.setCategorie(cat);
		    article.setUser(user);
		    article.setCle(cle);
		     try {
				article.setImg(Base64.getEncoder().encodeToString(img.getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		     Article result = ar.save(article);
		if (result.equals(article)) {
			return true;
		}
			else {
				return false;
			}
		}



	@Override
	public List<Article> allArticlesDirect() {
		return ar.findAll();
	}



	@Override
	public List<Article> findArticlesByUser(String Username) {
		User user = ur.findByUsername(Username).get();
		return ar.findByUser(user);
	}
	

}
