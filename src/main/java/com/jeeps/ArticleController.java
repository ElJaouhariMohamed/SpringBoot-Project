package com.jeeps;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpSession;
import com.jeeps.Entities.Article;
import com.jeeps.Entities.Categorie;
import com.jeeps.Entities.Ecole;
import com.jeeps.Entities.User;
import com.jeeps.dao.ArticleSrvcImpl;
import com.jeeps.dao.CategorieSrvcImpl;
import com.jeeps.dao.EcoleSrvcImpl;
import com.jeeps.dao.ImageSrvcImpl;
import com.jeeps.dao.UserSrvcImpl;
import utils.FileUtil;

@Controller
public class ArticleController {
	
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
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/{entity}/article/{id_school}")
	public String getALLArticles(@PathVariable long id_school, @PathVariable String entity , Model model  , HttpSession session) {
		
		if ((String) session.getAttribute("cle")!=null && (List<Article>) session.getAttribute("articles")!=null & 
				(Ecole) session.getAttribute("ecole")!=null) {
			
			String cle = (String) session.getAttribute("cle");
			session.removeAttribute("cle");
			List<Article> articles = (List<Article>) session.getAttribute("articles");
			Ecole ecole = (Ecole) session.getAttribute("ecole");
			model.addAttribute("cle", cle);
			model.addAttribute("ecole",ecole);
			model.addAttribute("articles",articles);
			return "article_liste_"+entity;
			
		}
		
		Ecole ecole = ecole_service.chercherEcole(id_school);
		List<Article> articles = article_service.allArticles(ecole);
		String cle = new String();
		
		model.addAttribute("cle", cle);
		model.addAttribute("ecole",ecole);
		model.addAttribute("articles",articles);
		return "article_liste_"+entity;
	}
	
	@GetMapping("/{entity}/article/{id_school}/{article_id}")
	public String getArticle(@PathVariable long id_school, @PathVariable long article_id, @PathVariable String entity ,Model model) {
		//if (id_school!=0) {
		Ecole ecole = ecole_service.chercherEcole(id_school);
		Article article_précis = article_service.getArticle(article_id);
		model.addAttribute("ecole",ecole);
		model.addAttribute("article",article_précis);
		User user= article_précis.getUser();
		model.addAttribute("user", user);
		
		System.out.println(entity);
		return "article";
		/*}else {
			Article article_précis = article_service.getArticle(article_id);
			model.addAttribute("article",article_précis);
			model.addAttribute("user", user);
			return "article";
		}*/
	}
	
	@GetMapping("/{entity}/article/{id_school}/newArticle")
	public String addArticle(@PathVariable long id_school, @PathVariable String entity ,Model model) {
		//if (id_school!=0) {
			String username = FileUtil.findUser();
			User user = user_service.findUser(username);
			Ecole ecole = ecole_service.chercherEcole(id_school);
			model.addAttribute("ecole",ecole);
			model.addAttribute("user", user);
			return "newArticle";
		/*}
		else {
			return "newArticle";
		}*/
		
	}
	
	@PostMapping("/{entity}/article/{id_school}/validForm")
	public String saveArticle( Model model, @PathVariable long id_school, @PathVariable String entity, @RequestParam("user") long user_id,
			@RequestParam("image") MultipartFile images , @RequestParam("title") String title , @RequestParam("message") String message , 
			@RequestParam("cle") String cle) {
	    try {
			article_service.ajouterArticleEcole(id_school, title,images,message,user_id,cle);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return "redirect:/"+entity+"/article/"+id_school;
	}
	
	@GetMapping("/{entity}/article/{id_school}/{article_id}/modify")
	public String modifierArticle(@PathVariable long id_school, @PathVariable long article_id, @PathVariable String entity,Model model) {
		String username = FileUtil.findUser();
		User user = user_service.findUser(username);
		Article article_ = article_service.getArticle(article_id);
		//model.addAttribute("entity",entity);
		model.addAttribute("article_",article_);
		model.addAttribute("user", user);
		return "modifierArticle";
		
	}
	
	@PostMapping("/{entity}/article/{id_school}/{article_id}/validModify")
	public String saveModifierArticle(@ModelAttribute("article_") Article article_,@PathVariable long id_school, @PathVariable long article_id , 
			@PathVariable String entity,Model model ,@RequestParam("image") MultipartFile images , @RequestParam("titre") String title ,
			@RequestParam("message") String message, @RequestParam("user") long user_id, @RequestParam("cle") String cle
			) {
		
		article_service.modifierArticle(article_,id_school, images, title, message,user_id,cle);
		
		return "redirect:/"+entity+"/article/"+String.valueOf(id_school);
	}
	
	
	/*
	@GetMapping("/{entity}/article/{article_id}/modify")
	public String getmodifierArticle(@PathVariable long article_id, @PathVariable String entity,Model model) {
		Article article_ = article_service.getArticle(article_id);
		model.addAttribute("article_",article_);
		return "modifierArticle";
	}*/
	
	
	@PostMapping("/{entity}/vie/{id_category}/search")
	public String searchArticleCategory(@RequestParam("cle") String cle,
			@PathVariable long id_category, 
			@PathVariable String entity,Model model , HttpSession session) {
		
		Optional<Categorie> category = categorie_service.CategorieByID(id_category);
		List<Article> articles = article_service.AllArticlesByclé(cle,id_category,"categorie",null);
		
		session.setAttribute("cle", cle);
		session.setAttribute("articles", articles);
		session.setAttribute("object", category.get());
		
		return "redirect:/" + entity + "/vie/" + id_category;
		
		//RedirectView redirectView = new RedirectView("/" + entity + "/vie/" + id_category, true);
		//return redirectView;
	}
	
	
	@PostMapping("/{entity}/article/{id_school}/search")
	public String searchArticleEcole(@RequestParam("cle") String cle,@PathVariable long id_school, 
			@PathVariable String entity,Model model , HttpSession session) {
		
		String username = FileUtil.findUser();
		User user = user_service.findUser(username);
		model.addAttribute("user", user);
		
		Ecole ecole = ecole_service.chercherEcole(id_school);
		List<Article> articles = article_service.AllArticlesByclé(cle,id_school,"school",null);
		
		session.setAttribute("cle", cle);
		session.setAttribute("articles", articles);
		session.setAttribute("ecole", ecole);
		
		return "redirect:/" + entity + "/article/" + id_school;
	}
	
	@PostMapping("/{entity}/list/articles/search")
	public String searchArticleAdmin(@RequestParam("cle") String cle, @PathVariable String entity,Model model , HttpSession session) {
		String username = FileUtil.findUser();
		User user = user_service.findUser(username);
		model.addAttribute("user", user);
		List<Article> articles = article_service.AllArticlesByclé(cle,0,entity,user);
		
		session.setAttribute("cle", cle);
		session.setAttribute("articles", articles);
		model.addAttribute("articles", articles);
		if (entity.equalsIgnoreCase("admin")) {
		return "redirect:/"+entity+"/list/articles";
		} else if (entity.equalsIgnoreCase("user")) {
			return "redirect:/"+entity+"/list/article";
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/user/list/article")
	public String getArticles(Model model,HttpSession session) {
		if ((String) session.getAttribute("cle")!=null && (List<Article>) session.getAttribute("articles")!=null ) {
			String cle = (String) session.getAttribute("cle");
			session.removeAttribute("cle");
			List<Article> articles = (List<Article>) session.getAttribute("articles");
			model.addAttribute("cle", cle);
			model.addAttribute("articles",articles);
			return "listArticle_portUser";
		}
		//get the user here
		String username = FileUtil.findUser();
		User user = user_service.findUser(username);
		List<Article> articles = article_service.findArticlesByUser(username);
		String cle ="";
		session.setAttribute("cle", cle);
		model.addAttribute("articles", articles);
		model.addAttribute("user", user);
		return "listArticle_portUser";
	}
	
	
	
	@GetMapping("/{entity}/{article}/{id}/{article_id}/modifyAdmin")
	public String getmodifierArticle(@PathVariable long id, @PathVariable long article_id, @PathVariable String entity,Model model,@PathVariable String article) {
		String username = FileUtil.findUser();
		User user = user_service.findUser(username);
		if (article.equalsIgnoreCase("articleport")) {
		Article article_ = article_service.getArticle(article_id);
		model.addAttribute("article_",article_);
		model.addAttribute("user", user);
		} else if (article.equalsIgnoreCase("vieport")) {
			Article article_ = article_service.getArticle(article_id);
			model.addAttribute("article_",article_);
			model.addAttribute("user", user);
		}
		return "modifierArticleAdminUser";
		
	}
	
	@PostMapping("/{entity}/{article}/{id}/{article_id}/validModifyAdmin")
	public String getmodifierArticle(@ModelAttribute("article_") Article article_,@PathVariable long id, @PathVariable long article_id , 
			@PathVariable String entity,Model model ,@RequestParam("image") MultipartFile images , @RequestParam("title") String title ,
			@RequestParam("message") String message, @RequestParam("user") long user_id,@PathVariable String article,@RequestParam("cle") String cle
			) {
		
		if (article.equalsIgnoreCase("articleport")) {
		article_service.modifierArticle(article_,id, images, title, message,user_id,cle);
		} else if (article.equalsIgnoreCase("vieport")) {
			categorie_service.modifierArticle(article_, id , images, title, message,user_id,cle);
		}
		if (entity.equalsIgnoreCase("user")) {
			return "redirect:/user/list/article";
		} else {
			return "redirect:/admin/list/articles";
		}
	}
	
	@GetMapping("/{entity}/article/{id_school}/{article_id}/delete")
	public String suppArticle(@PathVariable long id_school, @PathVariable long article_id, @PathVariable String entity ,Model model) {
		Article article_supp = article_service.getArticle(article_id);
		boolean result = article_service.supprimerArticle(article_supp);
		if (id_school !=0) {
			if (result == true) {
				return "redirect:/"+entity+"/article/"+String.valueOf(id_school);
			} else {
				return "redirect:/"+entity+"/article/"+String.valueOf(id_school)+"/"+String.valueOf(article_id);
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
}
