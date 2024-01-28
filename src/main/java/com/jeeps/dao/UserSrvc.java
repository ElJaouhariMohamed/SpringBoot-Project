package com.jeeps.dao;

import java.util.List;

import com.jeeps.Entities.User;

public interface UserSrvc {
	public boolean ajouterUser(User f);
	public boolean supprimerUser(long id);
	public User chercherUser(long id);
	public boolean modifierUser(long id, User u,String oldpass);
	public List<User> toutUser();
	public User findUser(String username);
	boolean modifierPass(long id, User u, String oldpass,String newpass);
}
