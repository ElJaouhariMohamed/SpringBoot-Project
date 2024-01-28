package com.jeeps;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.jeeps.Entities.Article;
import com.jeeps.Entities.Categorie;
import com.jeeps.Entities.User;
import com.jeeps.dao.ArticleSrvcImpl;
import com.jeeps.dao.CategorieSrvcImpl;
import com.jeeps.dao.EcoleSrvcImpl;
import com.jeeps.dao.ImageSrvcImpl;
import com.jeeps.dao.UserSrvcImpl;
import jakarta.servlet.http.HttpSession;
import utils.FileUtil;

@Controller
public class CategoryContr {

	@Autowired
	ArticleSrvcImpl article_service ;
	
	@Autowired
	EcoleSrvcImpl ecole_service;
	
	@Autowired 
	ImageSrvcImpl image_service;
	
	@Autowired
	CategorieSrvcImpl categorie_service;
	
	@Autowired
	UserSrvcImpl user_service;
	
	//went to GenCtr to control access to vie etudiante for public..
	@GetMapping("/{entity}/vie")
	public String getVie(@PathVariable String entity, Model model) {
		
		List<Categorie> categories = categorie_service.allCategorie();
		
		model.addAttribute("categories", categories);
		model.addAttribute("entity",entity);
		
		return "Vie_etudiante.html";
	}
	
	
	@GetMapping("/{entity}/vie/{category_id}")
	public String getCategorie(@PathVariable String entity, @PathVariable long category_id , Model model , HttpSession session) {
		/*
		if ((String) session.getAttribute("cle")!=null && (List<Article>) session.getAttribute("articles")!=null) {
			String cle = (String) session.getAttribute("cle");
			session.removeAttribute("cle");
			List<Article> articles = (List<Article>) session.getAttribute("articles");
			Categorie categorie = (Categorie) session.getAttribute("object");
			model.addAttribute("cle", cle);
			model.addAttribute("object",categorie);
			model.addAttribute("articles",articles);
			return "article_liste_category_"+entity;
		}*/
		
		Optional<Categorie> category = categorie_service.CategorieByID(category_id);
		List<Article> articles = categorie_service.allArticlesbyCategorie(category.get());
		String cle = new String();
		
		model.addAttribute("cle", cle);
		model.addAttribute("articles", articles);
		model.addAttribute("object", category.get());
		
		return "article_liste_category_"+entity;
	}
	
	@GetMapping("/{entity}/vie/{action}/{id}")
	public String APIHandlerRD(@PathVariable String entity,@PathVariable String action,@PathVariable long id, Model model) {
			long category_id = Long.parseLong(action);
			
			
			Optional<Categorie> category = categorie_service.CategorieByID(category_id);
			Article article = article_service.getArticle(id);
			User user = article.getUser();
			model.addAttribute("user", user);
			String cle = new String();
			model.addAttribute("cle", cle);
			model.addAttribute("article", article);
			model.addAttribute("object", category.get());
			return "articleCategory";
	}
	
	@GetMapping("/{entity}/vie/{category_id}/newArticle")
	public String addArticle(@PathVariable String entity, @PathVariable long category_id , Model model) {
		
			String username = FileUtil.findUser();
			User user = user_service.findUser(username);
			model.addAttribute("user", user);
			Article article = new Article();
			Optional<Categorie> category = categorie_service.CategorieByID(category_id);
			model.addAttribute("category",category.get());
			model.addAttribute("new_article",article);
			return "newArticle_category";
		
	}
	
	@PostMapping("/{entity}/vie/{category_id}/validForm")
	public String saveArticle(@ModelAttribute("new_article") Article article, Model model,@PathVariable long category_id,@PathVariable String entity
			,@RequestParam("image") MultipartFile images , @RequestParam("title") String title , @RequestParam("message") String message, @RequestParam("user") long user_id,@RequestParam("cle") String cle
			) throws IOException {
		article_service.ajouterArticleCategory(category_id, title, images, message,user_id,cle);
		return "redirect:/"+entity+"/vie/"+category_id;
	}
	@GetMapping("/{entity}/vie/{category_id}/{article_id}/delete")
	public String suppArticle( @PathVariable long category_id, @PathVariable long article_id, @PathVariable String entity ,Model model) {
		Article a = article_service.getArticle(article_id);
		boolean result = article_service.supprimerArticle(a);
		if(category_id!=0) {
		if (result) {
			return "redirect:/"+entity+"/vie/"+String.valueOf(category_id);
		} else {
			return "redirect:/"+entity+"/vie/"+String.valueOf(category_id)+"/"+String.valueOf(article_id);
		}
		}
		else {
			if (entity.equalsIgnoreCase("user")) {
			return "redirect:/user/list/article";
		} else {
			return "redirect:/admin/list/articles";
		}
		}
		
	}
	
	@GetMapping("/{entity}/vie/{category_id}/{article_id}/modify")
	public String getmodifierArticle(@PathVariable long category_id, @PathVariable long article_id, @PathVariable String entity,Model model) {
		String username = FileUtil.findUser();
		User user = user_service.findUser(username);
		model.addAttribute("user", user);
		Article article_ = article_service.getArticle(article_id);
		model.addAttribute("article_",article_);
		return "modifierArticle_Category";
	}
	
	@PostMapping("/{entity}/vie/{category_id}/{article_id}/validModify")
	public String getmodifierArticle(@ModelAttribute("article_") Article article_,@PathVariable long category_id, @PathVariable long article_id , @PathVariable String entity,Model model
			,@RequestParam("image") MultipartFile images , @RequestParam("title") String title , @RequestParam("message") String message , @RequestParam("user") long user_id,
			@RequestParam("cle") String cle
			) {
		
		boolean result = categorie_service.modifierArticle(article_, category_id , images, title, message,user_id,cle);
		if (result) {
		return "redirect:/"+entity+"/vie/"+String.valueOf(category_id);
		} else {
			return "redirect:/"+entity+"/vie/"+String.valueOf(category_id)+"/"+String.valueOf(article_id);
		}
	}
	
	// CATEGORIE ADD MODIFY AND DELETE FOR ADMIN PORTAL
	
	
	@GetMapping("/admin/vieadmin/newCat")
	public String addCategorie(Model model) {
			String username = FileUtil.findUser();
			User user = user_service.findUser(username);
			model.addAttribute("user", user);
			return "newCategorie";
		}
	

	@PostMapping("/admin/vieadmin/validnewcat")
	public String ConfirmaddCategorie(Model model,@RequestParam("image") MultipartFile images , 
			@RequestParam("title") String title,@RequestParam("user") long user_id) throws IOException {
		categorie_service.ajouterCategory(title, images,user_id);
		return "redirect:/admin/list/vieadmin";
	}

	@GetMapping("/admin/{category_id}/deleteCat")
	public String deleteCat(@PathVariable long category_id) {
		categorie_service.deleteCat(categorie_service.CategorieByID(category_id).get());
		
		return "redirect:/admin/list/vieadmin";
			
	}
	
	@GetMapping("/admin/vieadmin/modifyCat/{category_id}")
	public String modifierCategory(@PathVariable long category_id,Model model) {
		String username = FileUtil.findUser();
		User user = user_service.findUser(username);
		model.addAttribute("user", user);
		Categorie cat = categorie_service.CategorieByID(category_id).get();
		model.addAttribute("categorie",cat);
		return "modifierCategory";
	}
	
	
	@PostMapping("/admin/vieadmin/Confirmmodify")
	public String ConfirmmodifierCategory(Model model,@ModelAttribute("categorie") Categorie cat
			,@RequestParam("image") MultipartFile images , @RequestParam("title") String title , 
			@RequestParam("user") long user_id, @RequestParam("id") long id_categorie
			) throws IOException {
		
		categorie_service.modifierCat(title, images,user_id,id_categorie);
		
		return "redirect:/admin/list/vieadmin";
		
	}

}
	
	
	

