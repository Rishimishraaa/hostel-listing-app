package com.rk.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	
	
	

	public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
		super();
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}




	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String path = request.getServletPath();
		if(path.startsWith("/api/auth/") || path.startsWith("/api/common/")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		// 1. Get Authorization header
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;
		
		
		// 2. header check
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		jwt = authHeader.substring(7).trim(); // Bearer ke bad ka token
		
		try {
			// 3. extract username from token
			username = jwtService.extractUsername(jwt);
			
			// 4. agar username mila aur context empty hai
			
			if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				
				if(jwtService.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new
							UsernamePasswordAuthenticationToken(
									userDetails,null, userDetails.getAuthorities());
					
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					// 6. security context me authentication set kro
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			
		}
		catch (ExpiredJwtException e) {
			System.out.println("Expired jwt token :"+e.getMessage());
		}
		catch (UnsupportedJwtException | MalformedJwtException e) {
			System.out.println("Invalid Jwt : "+e.getMessage());
		}
		
		catch (Exception e) {
			System.out.println("Jwt Filter Error : "+e.getMessage());
		}
		
		
		filterChain.doFilter(request, response);
		
		
	}

}
