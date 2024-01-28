package com.jeeps.dao;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeps.Entities.Contact;

@Service
@Transactional
public class ContactSrvcImpl implements ContactSrvc{
	@Autowired
	private ContactRepo CR;
	
	@Override
	public boolean ajouterContact(Contact f) {
		Contact result = CR.save(f);
		if(result.equals(f)){
			return true;
		}
		return false;
	}

	@Override
	public boolean supprimerContact(long id) {
		try{
			if(CR.findById(id).get().getClass()==Contact.class) {
				CR.deleteById(id);
				return true;
			}
			return false;
		}
		catch(Exception e){
			return false;
		}
	}

	@Override
	public Contact chercherContact(long id) {
		return CR.findById(id).get();
	}

	@Override
	public boolean modifierContact(long id, Contact c) {
		Contact dbC = chercherContact(id);
		if(dbC!=null) {
			if(Objects.nonNull(c.getNumTel()) && !"".equalsIgnoreCase(c.getNumTel())){
				dbC.setNumTel(c.getNumTel());
			}
			if(Objects.nonNull(c.getNom()) && !"".equalsIgnoreCase(c.getNom())){
				dbC.setNom(c.getNom());
			}
			if(Objects.nonNull(c.getEmail()) && !"".equalsIgnoreCase(c.getEmail())){
				dbC.setEmail(c.getEmail());
			}
			if(Objects.nonNull(c.getAdresse()) && !"".equalsIgnoreCase(c.getAdresse())){
				dbC.setAdresse(c.getAdresse());
			}
			CR.save(dbC);
			return true;
		}
		return false;
	}
	
	public List<Contact> toutContact(){
		return CR.findAll();
	}

}
