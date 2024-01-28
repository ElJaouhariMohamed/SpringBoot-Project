package com.jeeps.dao;

import java.util.List;

import com.jeeps.Entities.Formation;

public interface FormationSrvc {
	public boolean ajouterFormation(Formation f);
	public boolean supprimerFormation(long id);
	public Formation chercherFormation(long id);
	public boolean modifierFormation(long id, Formation f);
	public List<Formation> toutFormation();
}
