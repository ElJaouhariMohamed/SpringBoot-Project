package com.jeeps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.jeeps.Entities.Article;
import com.jeeps.Entities.Categorie;
import com.jeeps.Entities.Ecole;
import com.jeeps.Entities.User;
import com.jeeps.dao.ArticleSrvcImpl;
import com.jeeps.dao.CategorieSrvcImpl;
import com.jeeps.dao.EcoleSrvcImpl;
import com.jeeps.dao.ImageSrvcImpl;
import com.jeeps.dao.UserRepo;
import com.jeeps.dao.UserSrvcImpl;

import utils.FileUtil;

@Controller
public class GenCntr {
	@Autowired
	UserSrvcImpl UserSrvcs;
	@Autowired
	EcoleSrvcImpl EcoleSrvcs;
	@Autowired
	UserRepo UR;
	@Autowired
	ArticleSrvcImpl article_service ;
	
	@Autowired
	EcoleSrvcImpl ecole_service;
	
	@Autowired 
	ImageSrvcImpl image_service;
	
	@Autowired
	CategorieSrvcImpl categorie_service;
	
	@GetMapping({"/","/public/accueil","/home","/error"})
	public String home(Model model) {
		model.addAttribute("ecoles",EcoleSrvcs.toutEcole() );
		return "acceuil.html";
	}
	
	@GetMapping("/{entity}/accueil")
	public String homeE(@PathVariable String entity,Model model) {
		model.addAttribute("ecoles",EcoleSrvcs.toutEcole() );
		return "acceuil.html";
	}
	
	@GetMapping("/profile/{username}")
	public String profileGen(@PathVariable String username,Model model) {
		User u = UserSrvcs.findUser(username);
		if(Objects.nonNull(u)) {
			model.addAttribute("user", u);
			return "profile.html";
		}
		else {
			return "acceuil.html";
		}
	}
	
	@PreAuthorize("hasAuthority('USER')")
	@GetMapping({"/profile","/user/profile","/user/profile/"})
	public String profile(Model model) {
		String username = FileUtil.findUser();
		User u = UserSrvcs.findUser(username);
		if(Objects.nonNull(u)) {
			model.addAttribute("status",new status(666,""));
			model.addAttribute("user", u);
			return "profile.html";
		}
		else {
			return "acceuil.html";
		}
	}
	
	@GetMapping("/user/modifier/profile")
	public String dashboard(Model model) {
		String username = FileUtil.findUser();
		User u = UserSrvcs.findUser(username);
		if(Objects.nonNull(u)) {
			model.addAttribute("user", u);
			model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
			return "modifierProfile.html";
		}
		else {
			return "profile.html";
		}
	}
	
	
	@GetMapping("/login") 
	public String Login(@RequestParam(name = "signup",required=false) String s,Model model) {
		if(Objects.nonNull(s) && s.equals("true")) {
			model.addAttribute("status",new status(200,"Bien inscrit! Connectez-vous !"));
		}
		return "login.html";
	}
	@GetMapping("/logout") 
	public String Logout() {
		return "redirect:/";
	}
	
	@GetMapping("/public/{entity}")
	public String GeneralAccess(@PathVariable String entity,@RequestParam(name = "uexist",required=false) String uexist,Model model) {
		if(Objects.nonNull(uexist) && uexist.equals("true")) {
			model.addAttribute("status",new status(500,"Choisissez un nom d'utilisateur different!"));
		}
		if(entity.equals("vie")) {
			List<Categorie> categories = categorie_service.allCategorie();
			
			model.addAttribute("categories", categories);
			model.addAttribute("entity", "public");
			
			return "Vie_etudiante";
		}
		if("signup terms Contact khouribga".contains(entity)) {
			return entity+".html";
		}else {
			return "redirect:/public/accueil";
		}
	}
	
	@PostMapping("/public/Signup/newU")
	public String signuserGeneralAccess(@ModelAttribute User u) {
		u.setRole("USER");
		u.setUserpass(u.getUserpass());
		if(!UR.findByUsername(u.getUsername()).isEmpty()) {
			return "redirect:/public/signup?uexist=true";
		}
		UserSrvcs.ajouterUser(u);
		return "redirect:/login?signup=true";
	}
	
	@GetMapping("/public/{entity}/{action}/{id}")
	public String GenAPIHandlerRD(@PathVariable String entity,@PathVariable String action,@PathVariable long id, Model model) {
		status st = null;
		
		if (entity.equalsIgnoreCase("vie")) {
			long category_id = Long.parseLong(action);
			Optional<Categorie> category = categorie_service.CategorieByID(category_id);
			Article article = article_service.getArticle(id);
			String cle = new String();
			model.addAttribute("cle", cle);
			model.addAttribute("article", article);
			model.addAttribute("object", category.get());
			return "articleCategory";
		}
		if(entity.equalsIgnoreCase("ecoles")) {
			Ecole e = EcoleSrvcs.chercherEcole(id);
			if(!e.equals(null)) {
				st = new status(200,"OK");
			}
			else {
				st = new status(500,"Erreur");
			}
			model.addAttribute("status", st);
			if(st.getCode()==200) {
				model.addAttribute("ecole",e);
				return "afficherEcole.html";
			}
			model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
			return "listEcoles.html";
		}
		if(entity.equalsIgnoreCase("users")) {
			User u = UserSrvcs.chercherUser(id);
			if(Objects.nonNull(u)) {
				st = new status(200,"User ");
					model.addAttribute("user",u);
					return "profile.html";
				
			}
			else {
				st = new status(500,"Erreur");
			}
			model.addAttribute("users", UserSrvcs.toutUser());
			model.addAttribute("status", st);
			return "listUsers.html";
		}
		model.addAttribute("status", st);
		st = new status(404,"Pas d'entite : "+entity);
		return "admin.html";
	}
	
	@PostMapping("/user/modifier/{id}")
	public String UserAPIHandlerUPD(@PathVariable long id,@ModelAttribute User u,@ModelAttribute upassChange pchg,Model model) {
		status st = null;
		String npage= "profile.html";
		if(!pchg.isChgPass()) {
		if(!UserSrvcs.modifierUser(id,u,u.getUserpass())) {
			st = new status(500,"Mot de passe incorrect!");
			model.addAttribute("status", st);
			npage="profile.html";
			}
		}else {
			if(!UserSrvcs.modifierPass(id,u,u.getUserpass(),pchg.getNewpass())) {
				st = new status(500,"Mot de passe incorrect, votre mot de passe n'est pas changé!");
				model.addAttribute("status", st);
				npage="profile.html";
				}
		}
		st = new status(200,"Changements appliqués avec succès");
		u = UserSrvcs.chercherUser(id);
		model.addAttribute("user",u);
		return npage;
	}
	
	//view articles form a category as user : 
	
	@GetMapping("/public/vie/{action}/{id}")
	public String APIHandlerRD(@PathVariable String action,@PathVariable long id, Model model) {
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
}

