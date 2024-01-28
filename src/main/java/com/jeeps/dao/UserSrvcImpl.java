package com.jeeps.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeeps.Entities.User;
import com.jeeps.Entities.Image;

@Service
@Transactional
public class UserSrvcImpl implements UserSrvc{
	@Autowired
	private UserRepo UR;
	@Autowired
	private ImageRepo IR;
	@Autowired
	private ImageSrvcImpl ImageSrvc;
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
	private EcoleSrvcImpl EcoleSrvcs;
	
	
	@Override
	public boolean ajouterUser(User u) {
		u.setUserpass(passwordEncoder.encode(u.getUserpass()));
		User result = UR.save(u);
		if(result.equals(u)){
			return true;
		}
		return false;
	}

	@Override
	public boolean supprimerUser(long id) {
			User u = UR.findById(id).get();
			if(Objects.nonNull(u)) {
				if(Objects.nonNull(u.getProfileimg())) {
					ImageSrvc.supprimerImage(u.getProfileimg().getImage_id());
				}
				UR.delete(u);
				return true;
			}
			return false;
	}

	@Override
	public User chercherUser(long id) {
		return UR.findById(id).get();
	}

	@Override
	public boolean modifierUser(long id, User u,String oldpass) {
		User dbU = chercherUser(id);
		if(Objects.nonNull(dbU) && passwordEncoder.matches(oldpass, dbU.getUserpass())) {
			if(Objects.nonNull(u.getUsername()) && !"".equalsIgnoreCase(u.getUsername())){
				dbU.setUsername(u.getUsername());
			}
			if(Objects.nonNull(u.getUserpass()) && !"".equalsIgnoreCase(u.getUserpass())){
				dbU.setUserpass(passwordEncoder.encode(u.getUserpass()));
			}
			if(Objects.nonNull(u.getRole())){
				dbU.setRole(u.getRole());
			}
			if(Objects.nonNull(u.getNom())){
				dbU.setNom(u.getNom());
			}
			if(Objects.nonNull(u.getPrenom())){
				dbU.setPrenom(u.getPrenom());
			}
			if(Objects.nonNull(u.getEcole())){
				String orgname = null;
				if(!Objects.nonNull(dbU.getEcole())) {
					orgname="";
				}else {
					orgname = dbU.getEcole().getNom();
				}
				if(!u.getEcole().getNom().equals(orgname) && u.getEcole().getNom()!="") {
					dbU.setEcole(EcoleSrvcs.toutEcoleNom(u.getEcole().getNom()).get(0));
				}else if(orgname!="" && u.getEcole().getNom()=="") {
					dbU.setEcole(null);
				}
			}
			if(Objects.nonNull(u.getInformations())){
				dbU.setInformations(u.getInformations());
			}
			if(Objects.nonNull(u.getProfileimg())){
				//search img first :
				List<Image> Is = IR.findByURL(u.getProfileimg().getURL());
				if(Is.size()==0) {
					//new img:
					Image nI = new Image();
					nI.setUser(dbU);
					nI.setURL(u.getProfileimg().getURL());
					nI.setDate(LocalDateTime.now().toString());
					ImageSrvc.ajouterImage(nI);
					dbU.setProfileimg(nI);
				}else {
					//img already in db 
					dbU.setProfileimg(Is.get(0));
				}
			}
			
			UR.save(dbU);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean modifierPass(long id, User u,String oldpass,String newpass) {
		User dbU = chercherUser(id);
		if(dbU!=null && passwordEncoder.matches(oldpass, dbU.getUserpass())) {
			if(Objects.nonNull(u.getUsername()) && !"".equalsIgnoreCase(u.getUsername())){
				dbU.setUsername(u.getUsername());
			}
			if(oldpass!=newpass){
				dbU.setUserpass(passwordEncoder.encode(newpass));
			}
			if(Objects.nonNull(u.getRole())){
				dbU.setRole(u.getRole());
			}
			if(Objects.nonNull(u.getProfileimg())){
				//search img first :
				List<Image> Is = IR.findByURL(u.getProfileimg().getURL());
				if(Is.size()==0) {
					//new img:
					Image nI = new Image();
					nI.setUser(dbU);
					nI.setURL(u.getProfileimg().getURL());
					nI.setDate(LocalDateTime.now().toString());
					ImageSrvc.ajouterImage(nI);
					dbU.setProfileimg(nI);
				}else {
					//img already in db 
					dbU.setProfileimg(Is.get(0));
				}
			}
			if(Objects.nonNull(u.getNom())){
				dbU.setNom(u.getNom());
			}
			if(Objects.nonNull(u.getPrenom())){
				dbU.setPrenom(u.getPrenom());
			}
			if(Objects.nonNull(u.getEcole())){
				String orgname = null;
				if(!Objects.nonNull(dbU.getEcole())) {
					orgname="";
				}else {
					orgname = dbU.getEcole().getNom();
				}
				if(!u.getEcole().getNom().equals(orgname) && u.getEcole().getNom()!="") {
					dbU.setEcole(EcoleSrvcs.toutEcoleNom(u.getEcole().getNom()).get(0));
				}else if(orgname!="" && u.getEcole().getNom()=="") {
					dbU.setEcole(null);
				}
			}
			if(Objects.nonNull(u.getInformations())){
				dbU.setInformations(u.getInformations());
			}
			
			UR.save(dbU);
			return true;
		}
		return false;
	}
	
	public List<User> toutUser(){
		List<User> us = UR.findAll();
		return us;
	}
	
	public List<User> toutUserName(String Username){
		if(Username=="") {
			return toutUser();
		}
		List<User> all = toutUser();
		List<User> l = new ArrayList<User>();
		for(User u:all) {
			if(u.getUsername().toLowerCase().contains(Username.toLowerCase())) {
				l.add(u);
			}
		}
		return l;
	}
	
	public List<User> toutUserNameRole(String Username,String role){
		System.out.println(Username);
		System.out.println(role);
		if(role.equals("tout")) {
			return toutUserName(Username);
		}
		List<User> all = toutUserName(Username);
		List<User> l = new ArrayList<User>();
		for(User u : all) {
			if(u.getRole().contains(role)) {
				l.add(u);
			}
		}
			
		
		return l;
	}

	public boolean modifierUser(long id, User u) {
		User dbU = chercherUser(id);
		if(dbU!=null) {
			if(Objects.nonNull(u.getUsername()) && !"".equalsIgnoreCase(u.getUsername())){
				dbU.setUsername(u.getUsername());
			}
			if(Objects.nonNull(u.getUserpass()) && !"".equalsIgnoreCase(u.getUserpass())){
				dbU.setUserpass(passwordEncoder.encode(u.getUserpass()));
			}
			if(Objects.nonNull(u.getRole())){
				dbU.setRole(u.getRole());
			}
			if(Objects.nonNull(u.getProfileimg())){
				dbU.setProfileimg(u.getProfileimg());
			}
			
			UR.save(dbU);
			return true;
		}
		return false;
	}

	@Override
	public User findUser(String username) {
		
		return UR.findByUsername(username).get();
	}
	}
