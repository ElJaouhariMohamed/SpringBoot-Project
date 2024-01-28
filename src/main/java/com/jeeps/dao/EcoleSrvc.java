package com.jeeps.dao;

import com.jeeps.Entities.Ecole;

public interface EcoleSrvc {
	public boolean ajouterEcole(Ecole e);
	public boolean supprimerEcole(long id);
	public Ecole chercherEcole(long id);
	boolean modifierEcole(long id, Ecole e);
}
