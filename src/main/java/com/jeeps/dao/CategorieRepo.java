package com.jeeps.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jeeps.Entities.Categorie;


public interface CategorieRepo extends JpaRepository<Categorie,Long> {
}
