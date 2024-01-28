package com.jeeps;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jeeps.Entities.Article;
import com.jeeps.Entities.Categorie;
import com.jeeps.Entities.Contact;
import com.jeeps.Entities.Ecole;
import com.jeeps.Entities.Formation;
import com.jeeps.Entities.Image;
import com.jeeps.Entities.User;
import com.jeeps.dao.ArticleSrvcImpl;
import com.jeeps.dao.CategorieSrvcImpl;
import com.jeeps.dao.ContactSrvcImpl;
import com.jeeps.dao.EcoleSrvcImpl;
import com.jeeps.dao.FornationSrvcImpl;
import com.jeeps.dao.ImageSrvcImpl;
import com.jeeps.dao.UserSrvcImpl;

import jakarta.servlet.http.HttpSession;
import utils.FileUtil;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminCntr {
	
	//Services :
		@Autowired
		ArticleSrvcImpl article_service ;
		
		@Autowired
		EcoleSrvcImpl ecole_service;
		
		@Autowired 
		ImageSrvcImpl image_service;
		
		@Autowired
		CategorieSrvcImpl categorie_service;
		@Autowired
		EcoleSrvcImpl EcoleSrvcs;
		@Autowired
		FornationSrvcImpl FormationSrvcs;
		@Autowired
		ContactSrvcImpl ContactSrvcs;
		@Autowired
		ImageSrvcImpl ImageSrvcs;
		@Autowired
		UserSrvcImpl UserSrvcs;
	
	//Delete and Read handler
	//Ecoles
	@GetMapping("/admin/{entity}/{action}/{id}")
	public String APIHandlerRD(@PathVariable String entity,@PathVariable String action,@PathVariable long id, Model model) {
		status st = null;
		
		if (entity.equalsIgnoreCase("vie")) {
			String username = FileUtil.findUser();
			User user = UserSrvcs.findUser(username);
			model.addAttribute("user", user);
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
			if(action.equals("rechercher") && st.getCode()==200) {
				model.addAttribute("ecole",e);
				return "afficherEcole";
			}
			if(action.equals("supprimer") && st.getCode()==200) {
				EcoleSrvcs.supprimerEcole(id);		
			}
			model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
			return "listEcoles";
		}
		if(entity.equalsIgnoreCase("users")) {
			User u = UserSrvcs.chercherUser(id);
			if(Objects.nonNull(u)) {
				st = new status(200,"User ");
				if(action.equals("rechercher")) {
					model.addAttribute("user",u);
					return "profile";
				}
				else {
					if(UserSrvcs.supprimerUser(id)) {
						st.setMessage(st.getMessage()+" supprimer avec succes");
					}else {
						st.setCode(500);
						st.setMessage(st.getMessage()+" n'est pas supprimer ");
					}
				}
			}
			else {
				st = new status(500,"Erreur");
			}
			model.addAttribute("users", UserSrvcs.toutUser());
			model.addAttribute("status", st);
			return "listUsers";
		}
		model.addAttribute("status", st);
		st = new status(404,"Pas d'entite : "+entity);
		return "admin";
	}
	
	@GetMapping("/admin/{entity}/{action}/{idEcole}/{id}")
	public String EcoleAPIHandlerRD(@PathVariable String entity,@PathVariable String action,@PathVariable long idEcole,@PathVariable long id, Model model) {
		status st = new status(200,"");
		Ecole e = EcoleSrvcs.chercherEcole(idEcole);
		if(entity.equalsIgnoreCase("formation")) {
			Formation f = FormationSrvcs.chercherFormation(id);
			if(!Objects.nonNull(f) || !Objects.nonNull(e)) {
				st = new status(500,"Erreur");
				model.addAttribute("status", st);
			}
			
			if(action.equals("rechercher") && st.getCode()!=500) {
				model.addAttribute("ecole",e);
				return "afficherEcole";
			}
			if(action.equals("supprimer") && st.getCode()!=500) {
				FormationSrvcs.supprimerFormation(id);
			}
			model.addAttribute("ecole", e);
			return "listFormations";
		}
		if(entity.equalsIgnoreCase("contact")) {
			Contact c = ContactSrvcs.chercherContact(id);
			if(!Objects.nonNull(c) || !Objects.nonNull(e)){
				st = new status(500,"Erreur");
				model.addAttribute("status", st);
			}
			if(action.equals("rechercher") && st.getCode()!=500) {
				model.addAttribute("ecole",e);
				return "afficherEcole";
			}
			if(action.equals("supprimer") && st.getCode()!=500) {
				ContactSrvcs.supprimerContact(id);
			}
			model.addAttribute("ecole", e);
			return "listContacts";
		}
		if(entity.equalsIgnoreCase("image")) {
			Image i = ImageSrvcs.chercherImage(id);
			if(!Objects.nonNull(i) || !Objects.nonNull(e)) {
				st = new status(500,"Erreur");
				model.addAttribute("status", st);
			}
			if(action.equals("rechercher") && st.getCode()!=500) {
				model.addAttribute("ecole",e);
				return "afficherEcole";
			}
			if(action.equals("supprimer") && st.getCode()!=500) {
				ImageSrvcs.supprimerImage(id);
			}
			model.addAttribute("ecole", e);
			return "listImages";
		}
		return "listEcoles";
	}
	
	//Create and Update Handlers
	//Ecole
	@PostMapping("/admin/ecoles/ajouter")
	public String EcoleAPIHandlerC(@ModelAttribute  Ecole e,Model model) {
		status st = null;
		String npage= "redirect:/admin/list/ecoles";
		if(EcoleSrvcs.ajouterEcole(e)) {
			st = new status(200,"Ecole ajoutée avec succès");	
		}
		else {
			st = new status(500,"Erreur");
			npage="ajouterEcoles";
			}
		model.addAttribute("status", st);
		model.addAttribute("ecole",e);
		model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
		return npage;
	}
	
	
	@PostMapping("/admin/ecoles/modifier/{id}")
	public String EcoleAPIHandlerU(@PathVariable long id,@ModelAttribute Ecole e,Model model) {
		status st = null;
		String npage= "listEcoles";
		if(EcoleSrvcs.modifierEcole(id,e)) {
			st = new status(200,"Ecole modifiée avec succès");	
		}
		else {
			st = new status(500,"Erreur");
			npage="modifierEcole";
			}
		model.addAttribute("status", st);
		model.addAttribute("ecole",e);
		model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
		return npage;
	}
	
	//Create and Update Handlers
		//User
		@PostMapping("/admin/users/ajouter")
		public String UserAPIHandlerC(@ModelAttribute  User u,@ModelAttribute Image i,Model model) {
			status st = null;
			String npage= "listUsers";
			if(i.getURL().startsWith("http")) {
				i.setDate(LocalTime.now().toString());
				i.setTitre(u.getUsername()+" photo de profile");
				ImageSrvcs.ajouterImage(i);
				u.setProfileimg(i);
			}
			if(UserSrvcs.ajouterUser(u)) {
				st = new status(200,"User ajoutée avec succès");	
			}
			else {
				st = new status(500,"Erreur");
				npage="ajouterUsers";
				}
			model.addAttribute("status", st);
			model.addAttribute("user",u);
			model.addAttribute("users", UserSrvcs.toutUser());
			return npage;
		}
	
	//Formation
	//Create and Update Handler
		@PostMapping("/admin/formation/ajouter/{id}")
		public String FormationAPIHandlerC(@PathVariable long id,@ModelAttribute  Formation f,Model model) {
			status st = null;
			String npage= "listFormations";
			Ecole e = EcoleSrvcs.chercherEcole(id);
			if(!Objects.nonNull(e)) {
				st = new status(500,"Erreur");
				npage="ajouterEcole";	
			}
			else {
				List<Formation> flist = e.getFormations();
				if(!Objects.nonNull(flist)) {
					flist = new ArrayList<Formation>();
				}
				flist.add(f);
				e.setFormations(flist);
				f.setEcole(e);
				FormationSrvcs.ajouterFormation(f);
				EcoleSrvcs.modifierEcole(id, e);	
			}
			model.addAttribute("status", st);
			model.addAttribute("ecole",e);
			model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
			return npage;
		}
		
		@PostMapping("/admin/formation/modifier/{idEcole}/{id}")
		public String FormationAPIHandlerU(@PathVariable long idEcole,@PathVariable long id,@ModelAttribute  Formation f,Model model) {
			status st = null;
			String npage= "listFormations";
			Ecole e = EcoleSrvcs.chercherEcole(idEcole);
			if(!Objects.nonNull(e)) {
				st = new status(500,"Erreur");
				npage="ajouterEcole";	
			}
			else {
				List<Formation> flist = e.getFormations();
				List<Formation> nflist = new ArrayList<Formation>();
				if(!Objects.nonNull(flist)) {
					st = new status(404,"Aucune formation à modifier, ajoutez une!");
					npage="ajouterFormation";	
				}
				
				for(Formation dbF:flist) {
					if(dbF.getFormation_id()!=id) {
						nflist.add(dbF);
					}
				}
				f.setFormation_id(id);
				f.setEcole(e);
				nflist.add(f);
				e.setFormations(nflist);
				FormationSrvcs.modifierFormation(id,f);
				EcoleSrvcs.modifierEcole(id, e);	
			}
			model.addAttribute("status", st);
			model.addAttribute("ecole",e);
			return npage;
		}
		
		//Contact
		//Create and Update Handler
			@PostMapping("/admin/contact/ajouter/{id}")
			public String ContactAPIHandlerC(@PathVariable long id,@ModelAttribute  Contact c,Model model) {
				status st = null;
				String npage= "listContacts";
				Ecole e = EcoleSrvcs.chercherEcole(id);
				if(!Objects.nonNull(e)) {
					st = new status(500,"Erreur: Ecole introuvable");
					npage="ajouterEcole";	
				}
				else {
					List<Contact> clist = e.getContacts();
					if(!Objects.nonNull(clist)) {
						clist = new ArrayList<Contact>();
					}
					clist.add(c);
					e.setContacts(clist);
					c.setEcole(e);
					ContactSrvcs.ajouterContact(c);
					EcoleSrvcs.modifierEcole(id, e);	
				}
				model.addAttribute("status", st);
				model.addAttribute("ecole",e);
				model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
				return npage;
			}
			@PostMapping("/admin/contact/modifier/{idEcole}/{id}")
			public String ContactAPIHandlerU(@PathVariable long idEcole,@PathVariable long id,@ModelAttribute  Contact c,Model model) {
				status st = null;
				String npage= "listContacts";
				Ecole e = EcoleSrvcs.chercherEcole(idEcole);
				if(!Objects.nonNull(e)) {
					st = new status(500,"Erreur");
					npage="ajouterEcole";	
				}
				else {
					List<Contact> clist = e.getContacts();
					List<Contact> nclist = new ArrayList<Contact>();
					if(!Objects.nonNull(clist)) {
						st = new status(404,"Aucun contact à modifier, ajoutez une!");
						npage="ajouterContacts";	
					}
					
					for(Contact dbC:clist) {
						if(dbC.getContact_id()!=id) {
							nclist.add(dbC);
						}
					}
					c.setContact_id(id);
					c.setEcole(e);
					nclist.add(c);
					e.setContacts(nclist);
					ContactSrvcs.modifierContact(id,c);
					EcoleSrvcs.modifierEcole(id, e);	
				}
				model.addAttribute("status", st);
				model.addAttribute("ecole",e);
				return npage;
			}
			
			//Image
			//Create and Update Handler
				@PostMapping("/admin/image/ajouter/{id}")
				public String ImageAPIHandlerC(@PathVariable long id,@ModelAttribute  Image i,Model model) {
					status st = null;
					String npage= "listImages";
					Ecole e = EcoleSrvcs.chercherEcole(id);
					if(!Objects.nonNull(e)) {
						st = new status(500,"Erreur: Ecole introuvable");
						npage="ajouterEcole";	
					}
					else {
						List<Image> ilist = e.getImages();
						if(!Objects.nonNull(ilist)) {
							ilist = new ArrayList<Image>();
						}
						ilist.add(i);
						e.setImages(ilist);
						i.setEcole(e);
						i.setDate(LocalDateTime.now().toString());
						ImageSrvcs.ajouterImage(i);
						EcoleSrvcs.modifierEcole(id, e);	
					}
					model.addAttribute("status", st);
					model.addAttribute("ecole",e);
					model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
					return npage;
				}
				@PostMapping("/admin/image/modifier/{idEcole}/{id}")
				public String ImageAPIHandlerU(@PathVariable long idEcole,@PathVariable long id,@ModelAttribute  Image i,Model model) {
					status st = null;
					String npage= "listImages";
					Ecole e = EcoleSrvcs.chercherEcole(idEcole);
					if(!Objects.nonNull(e)) {
						st = new status(500,"Erreur");
						npage="ajouterEcole";	
					}
					else {
						List<Image> ilist = e.getImages();
						List<Image> nilist = new ArrayList<Image>();
						if(!Objects.nonNull(ilist)) {
							st = new status(404,"Aucun contact à modifier, ajoutez une!");
							npage="ajouterContacts";	
						}
						
						for(Image dbI:ilist) {
							if(dbI.getImage_id()!=id) {
								nilist.add(dbI);
							}
						}
						i.setImage_id(id);
						i.setEcole(e);
						nilist.add(i);
						e.setImages(nilist);
						ImageSrvcs.modifierImage(id,i);
						EcoleSrvcs.modifierEcole(id, e);	
					}
					model.addAttribute("status", st);
					model.addAttribute("ecole",e);
					return npage;
				}
	
	//Entity Listing 
	@SuppressWarnings("unchecked")
	@GetMapping("/admin/list/{entity}")
	public String adminNavigationListing(@PathVariable String entity,Model model,HttpSession session) {
		if ((String) session.getAttribute("cle")!=null && (List<Article>) session.getAttribute("articles")!=null) {
			String cle = (String) session.getAttribute("cle");
			List<Article> articles = (List<Article>) session.getAttribute("articles");
			session.removeAttribute("cle");
			session.removeAttribute("articles");
			model.addAttribute("cle", cle);
			model.addAttribute("articles",articles);
			return "listArticle_portAdmin";
		}
		if(entity.equalsIgnoreCase("ecoles")) {
			model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
			return "listEcoles";
		}
		if(entity.equalsIgnoreCase("users")) {
			model.addAttribute("users", UserSrvcs.toutUser());
			return "listUsers";
		}
		if(entity.equalsIgnoreCase("articles")) {
			model.addAttribute("articles", article_service.allArticlesDirect());
			return "listArticle_portAdmin";
		}
		if(entity.equalsIgnoreCase("vieadmin")) {
			model.addAttribute("categories", categorie_service.allCategorie());
			return "listVie_portAdmin";
		}
		return "admin";
	}
	
	//Entity Listing for sub entities of ecole
		@GetMapping("/admin/list/{entity}/ecole/{id}")
		public String adminNavigationListingSub(@PathVariable String entity,@PathVariable long id,Model model) {
			status st = null;
			Ecole e = EcoleSrvcs.chercherEcole(id);
			if(!Objects.nonNull(e)) {
				st= new status(404,"Ecole non existante!!");
				model.addAttribute("status",st);
			}
			else {
				String entities = "Formations,Articles,Contacts,Images";
				entity = entity.substring(0,1).toUpperCase()+entity.substring(1).toLowerCase()+"s";
				if(entities.contains(entity)) {
					model.addAttribute("ecole",e);
					return "list"+entity+"";
				}else {
					st= new status(404,"Entité invalide!!");
				}
			}
			return "listEcoles";
		}
	
	//Entity Adding
		@GetMapping("/admin/ajouter/{entity}")
		public String adminNavigationAjouter(@PathVariable String entity) {
			if(entity.equalsIgnoreCase("ecoles")) {
				return "ajouterEcoles";
			}
			if(entity.equalsIgnoreCase("users")) {
				return "ajouterUsers";
			}
			return "admin";
		}
	//Entity adding for related one (formation...)
		@GetMapping("/admin/ajouter/{entity}/ecole/{id}")
		public String adminNavigationAjouterSub(@PathVariable String entity,@PathVariable long id,Model model) {
			status st = null;
			Ecole e = EcoleSrvcs.chercherEcole(id);
			if(!Objects.nonNull(e)) {
				st= new status(404,"Ecole non existante!!");
				model.addAttribute("status",st);
			}
			else {
				String entities = "Formations,Articles,Contacts,Images";
				entity = entity.substring(0,1).toUpperCase()+entity.substring(1).toLowerCase()+"s";
				if(entities.contains(entity)) {
					model.addAttribute("ecole",e);
					return "ajouter"+entity+"";
				}else {
					st= new status(404,"Entité invalide!!");
				}
			}
			return "listEcoles";
		}
		
	//Entity Editting
	@GetMapping("/admin/modifier/{entity}/{id}")
	public String adminNavigationModifier(@PathVariable String entity,@PathVariable long id,Model model) {
			if(entity.equalsIgnoreCase("ecoles")) {
				Ecole e =EcoleSrvcs.chercherEcole(id);
				if(!e.equals(null)) {
					model.addAttribute("ecole",e);
					return "modifierEcole";
				}
				model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
				model.addAttribute("status",new status(404,"Pas d'ecole avec cet id."));
				return "listEcoles";
			}
			if(entity.equalsIgnoreCase("users")) {
				User u =UserSrvcs.chercherUser(id);
				if(!u.equals(null)) {
					model.addAttribute("user",u);
					return "modifierUser";
				}
				model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
				model.addAttribute("status",new status(404,"Pas d'ecole avec cet id."));
				return "listEcoles";
			}
			if(entity.equalsIgnoreCase("formation")) {
				Formation f =FormationSrvcs.chercherFormation(id);
				if(!f.equals(null)) {
					model.addAttribute("formation",f);
					return "modifierFormation";
				}
				model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
				model.addAttribute("status",new status(404,"Pas de formation avec cet id."));
				return "listEcoles";
			}
			if(entity.equalsIgnoreCase("contact")) {
				Contact c =ContactSrvcs.chercherContact(id);
				if(!c.equals(null)) {
					model.addAttribute("contact",c);
					return "modifierContact";
				}
				model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
				model.addAttribute("status",new status(404,"Pas de contact avec cet id."));
				return "listEcoles";
			}
			if(entity.equalsIgnoreCase("image")) {
				Image i = ImageSrvcs.chercherImage(id);
				if(!i.equals(null)) {
					model.addAttribute("image",i);
					return "modifierImage";
				}
				model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
				model.addAttribute("status",new status(404,"Pas d'image avec cet id."));
				return "listEcoles";
			}
			return "admin";
		}	
	
	//Search Controller 
	//Ecoles
	@GetMapping("/admin/rechParNom/ecoles")
	public String adminSearchEcole(@RequestParam(name = "ecoleNom") String ecoleNom,@RequestParam(name = "ecoleNiveau") String ecoleNiveau,Model model) {
			List<Ecole> e =EcoleSrvcs.toutEcoleNomNiveau(ecoleNom,ecoleNiveau);
			if(e.size()!=0) {
				model.addAttribute("ecoles",e);
				return "listEcoles";
			}
			model.addAttribute("ecoles", EcoleSrvcs.toutEcole());
			model.addAttribute("status",new status(404,"Aucune école n'est retrouvée"));
			return "listEcoles";
	}
	//Users
	@GetMapping("/admin/rechParNom/users")
	public String adminSearchUser(@RequestParam(name = "usernom") String usernom,@RequestParam(name = "role") String role,Model model) {
			List<User> us = UserSrvcs.toutUserNameRole(usernom, role);
			if(us.size()!=0) {
				model.addAttribute("users",us);
				return "listUsers";
			}
			model.addAttribute("users", UserSrvcs.toutUser());
			model.addAttribute("status",new status(404,"Aucun utilisateur n'est retrouvée"));
			return "listUsers";
	}
	
	//Navigation Admin General :
	
	@GetMapping({"/admin","/admin/"})
	public String adminGeneralAccess() {
		return "admin";
	}
	
	
}
