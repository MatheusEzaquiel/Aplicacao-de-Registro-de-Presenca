package com.mbe.ada.infra.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mbe.ada.model.user.User;
import com.mbe.ada.repository.IUserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private IUserRepository userRepos;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepos.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}

}
