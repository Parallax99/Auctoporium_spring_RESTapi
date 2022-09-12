package com.service.JWTutils;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.bean.RoleBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JwtTokenManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final long TOKEN_VALIDITY = 10 * 60 * 60;
	@Value("${jwt.secret}")
	private String jwtSecret;
	@Autowired
	private JwtUserDetails userDetails;

	@PostConstruct
	protected void init() {
		jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
	}

	public String generateJwtToken(String email, List<RoleBean> roles) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("auth", roles);
		return Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public Boolean validateJwtToken(String token) throws Exception {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			return true;
		} catch (JwtException |

				IllegalArgumentException e) {
			throw new Exception("Expired or invalid JWT token");
		}
	}

	public String getUsernameFromToken(String token) {
		final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		log.info(claims.getSubject() + "get user");
		return claims.getSubject();
	}

	public String resolveToken(HttpServletRequest request) {
		String bToken = request.getHeader("Authorization");
		if (bToken != null && bToken.startsWith("Bearer")) {
			return bToken.substring(7, bToken.length());
		} else {
			return null;
		}
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = this.userDetails.loadUserByUsername(getUsernameFromToken(token));
		log.info(userDetails + "auth");
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
				userDetails.getAuthorities());
	}
}