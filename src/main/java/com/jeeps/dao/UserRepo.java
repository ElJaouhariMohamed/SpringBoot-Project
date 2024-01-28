package com.jeeps.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeeps.Entities.User;

public interface UserRepo extends JpaRepository<User,Long>{
	Optional<User> findByUsername(String username);
}