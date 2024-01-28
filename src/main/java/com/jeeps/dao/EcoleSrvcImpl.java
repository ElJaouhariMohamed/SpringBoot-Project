package com.jeeps.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeps.Entities.Ecole;

@Service
@Transactional
public class EcoleSrvcImpl implements EcoleSrvc{
	@Autowired
	private EcoleRepo ER;
	

	@Override
	public boolean ajouterEcole(Ecole e) {
		Ecole result = ER.save(e);
		if(result.equals(e)){
			return true;
		}
		return false;
	}

	@Override
	public boolean supprimerEcole(long id) {
		try{
			if(ER.findById(id).get().getClass()==Ecole.class) {
				ER.deleteById(id);
				return true;
			}
			return false;
		}
		catch(Exception e){
			return false;
		}	
	}

	@Override
	public Ecole chercherEcole(long id) {
		return ER.findById(id).get();
	}

	@Override
	public boolean modifierEcole(long id ,Ecole e) {
		Ecole dbE = chercherEcole(id);
		if(dbE!=null) {
			if(Objects.nonNull(e.getNiveau()) && !"".equalsIgnoreCase(e.getNiveau())){
				dbE.setNiveau(e.getNiveau());
			}
			if(Objects.nonNull(e.getNom()) && !"".equalsIgnoreCase(e.getNom())){
				dbE.setNom(e.getNom());
			}
			if(Objects.nonNull(e.getAnncreation())) {
				dbE.setAnncreation(e.getAnncreation());
			}
			if(Objects.nonNull(e.getNbrEtd())) {
				dbE.setNbrEtd(e.getNbrEtd());
			}
			if(Objects.nonNull(e.getNbrStaff())) {
				dbE.setNbrStaff(e.getNbrStaff());
			}
			ER.save(dbE);
			return true;
		}
		return false;
	}
	
	
	public List<Ecole> toutEcole(){
		return ER.findAll();
	}
	
	public List<Ecole> toutEcoleNom(String nom){
		List<Ecole> all = toutEcole();
		List<Ecole> Selected = new ArrayList<Ecole>();
		for(Ecole e:all){
			if(e.getNom().toLowerCase().contains(nom.toLowerCase())) {
				Selected.add(e);
			}
		}
		return Selected;
	}
	
	public List<Ecole> toutEcoleNomNiveau(String nom,String niveau){
		List<Ecole> all = toutEcoleNom(nom);
		if(niveau.equalsIgnoreCase("tout")) {
			return all;
		}
		List<Ecole> Selected = new ArrayList<Ecole>();
		for(Ecole e:all){
			if(e.getNiveau().toLowerCase().contains(niveau.toLowerCase())) {
				Selected.add(e);
			}
		}
		return Selected;
	}
	
}
