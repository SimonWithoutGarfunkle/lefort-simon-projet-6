package com.openclassrooms.paymybuddy.model;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MyUserDetails implements UserDetails {
	
	private Utilisateur utilisateur;
	
	public MyUserDetails(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(utilisateur.getRole().toString());
        return Arrays.asList(authority);
    }
 
    @Override
    public String getPassword() {
        return utilisateur.getMotDePasse();
    }
 
    @Override
    public String getUsername() {
        return utilisateur.getEmail();
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
