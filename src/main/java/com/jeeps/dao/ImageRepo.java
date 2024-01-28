package com.jeeps.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeeps.Entities.Image;

public interface ImageRepo extends JpaRepository<Image,Long>{
	List<Image> findByURL(String uRL);
}
