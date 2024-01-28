package com.jeeps.dao;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeps.Entities.Formation;

@Service
@Transactional
public class FornationSrvcImpl implements FormationSrvc{
	@Autowired
	private FormationRepo FR;
	
	@Override
	public boolean ajouterFormation(Formation f) {
		Formation result = FR.save(f);
		if(result.equals(f)){
			return true;
		}
		return false;
	}

	@Override
	public boolean supprimerFormation(long id) {
		try{
			if(FR.findById(id).get().getClass()==Formation.class) {
				FR.deleteById(id);
				return true;
			}
			return false;
		}
		catch(Exception e){
			return false;
		}
	}

	@Override
	public Formation chercherFormation(long id) {
		return FR.findById(id).get();
	}

	@Override
	public boolean modifierFormation(long id, Formation f) {
		Formation dbF = chercherFormation(id);
		if(dbF!=null) {
			if(Objects.nonNull(f.getNiveau()) && !"".equalsIgnoreCase(f.getNiveau())){
				dbF.setNiveau(f.getNiveau());
			}
			if(Objects.nonNull(f.getNom()) && !"".equalsIgnoreCase(f.getNom())){
				dbF.setNom(f.getNom());
			}
			if(Objects.nonNull(f.getFrais()) && !"".equalsIgnoreCase(f.getFrais())){
				dbF.setFrais(f.getFrais());
			}
			if(Objects.nonNull(f.getDuree()) && !"".equalsIgnoreCase(f.getDuree())){
				dbF.setDuree(f.getDuree());
			}
			FR.save(dbF);
			return true;
		}
		return false;
	}
	
	public List<Formation> toutFormation(){
		return FR.findAll();
	}

}
