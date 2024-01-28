package com.jeeps.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeeps.Entities.Contact;

public interface ContactRepo extends JpaRepository<Contact,Long>{

}
