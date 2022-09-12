package com.service.JWTutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	@Autowired
	private JwtTokenManager jwtTokenManager;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
