package com.jeeps.dao;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeps.Entities.Image;

@Service
@Transactional
public class ImageSrvcImpl implements ImageSrvc{
	@Autowired
	private ImageRepo FR;
	
	@Override
	public boolean ajouterImage(Image f) {
		Image result = FR.save(f);
		if(result.equals(f)){
			return true;
		}
		return false;
	}

	@Override
	public boolean supprimerImage(long id) {
		try{
			if(FR.findById(id).get().getClass()==Image.class) {
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
	public Image chercherImage(long id) {
		return FR.findById(id).get();
	}

	@Override
	public boolean modifierImage(long id, Image f) {
		Image dbF = chercherImage(id);
		if(dbF!=null) {
			if(Objects.nonNull(f.getTitre()) && !"".equalsIgnoreCase(f.getTitre())){
				dbF.setTitre(f.getTitre());
			}
			if(Objects.nonNull(f.getDescription()) && !"".equalsIgnoreCase(f.getDescription())){
				dbF.setDescription(f.getDescription());
			}
			if(Objects.nonNull(f.getURL()) && !"".equalsIgnoreCase(f.getURL())){
				dbF.setURL(f.getURL());
			}
			FR.save(dbF);
			return true;
		}
		return false;
	}
	
	public List<Image> toutImage(){
		return FR.findAll();
	}

}
