package com.jeeps.dao;

import java.util.List;

import com.jeeps.Entities.Contact;

public interface ContactSrvc {
	public boolean ajouterContact(Contact f);
	public boolean supprimerContact(long id);
	public Contact chercherContact(long id);
	public boolean modifierContact(long id, Contact f);
	public List<Contact> toutContact();
}
