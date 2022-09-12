package com.service.JWTutils;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.bean.UserBean;
import com.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JwtUserDetails implements UserDetailsService {
	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info(email);
		UserBean user = userRepo.findByEmail(email);
		if (user != null) {
			log.info("email found");
			return new User(user.getEmail(), user.getPassword(), getGrantedAuthority(user));
		} else {
			throw new UsernameNotFoundException("User Not Found of Email:" + email);
		}
	}

	private Collection<? extends GrantedAuthority> getGrantedAuthority(UserBean user) {
		log.info("collection");
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		log.info("below collection");
		if (user.getRoles().get(0).getRoleName().equalsIgnoreCase("admin")) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return authorities;
	}

}
