package com.jeeps.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jeeps.Entities.User;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class myUserDetails implements UserDetails{
	private String username;
    private String userpass;
    private List<GrantedAuthority> authorities;

    public myUserDetails(User user) {
        username=user.getUsername();
        userpass=user.getUserpass();
        authorities = new ArrayList<GrantedAuthority>();
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority(user.getRole());
        authorities.add(auth);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userpass;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
