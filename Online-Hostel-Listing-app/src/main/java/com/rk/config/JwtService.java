package com.rk.config;

import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final String SECRETE_KEY = "m3XcN2y7K0FjY9p8Q1s4Z6r2T0uVn+PkR3dHjBqLm5wXc6Zs9l8Yt7HjGf4Vr9Wb";
	
	public String generateToken(UserDetails details) {
		return Jwts.builder()
				.setSubject(details.getUsername())
				.claim("role", details.getAuthorities()
						.stream().map(GrantedAuthority::getAuthority).findFirst().orElse("STUDENT"))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+24 * 60 * 60 * 1000))
				.signWith(Keys.hmacShaKeyFor(SECRETE_KEY.getBytes()),SignatureAlgorithm.HS256)
				.compact();
	}
	

	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(SECRETE_KEY.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public String extractRole(String token) {
		
				String role  =(String) Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(SECRETE_KEY.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody()
				.get("role");
				return role;
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		Date expiration = Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(SECRETE_KEY.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getExpiration();
		
		return expiration.before(new Date());
	}
	
}
