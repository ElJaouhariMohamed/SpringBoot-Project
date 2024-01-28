package com.jeeps.dao;

import java.util.List;

import com.jeeps.Entities.Image;

public interface ImageSrvc {
	public boolean ajouterImage(Image f);
	public boolean supprimerImage(long id);
	public Image chercherImage(long id);
	public boolean modifierImage(long id, Image f);
	public List<Image> toutImage();
}
