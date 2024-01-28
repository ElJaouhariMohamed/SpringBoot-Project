package com.jeeps.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import com.jeeps.Entities.User;

import com.jeeps.dao.UserRepo;

@Component
public class UserDetailsSrvc implements UserDetailsService{
	@Autowired
    private UserRepo UR;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = UR.findByUsername(username);
        return user.map(myUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
}
