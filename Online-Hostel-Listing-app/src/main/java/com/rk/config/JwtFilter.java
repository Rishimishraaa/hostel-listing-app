package com.rk.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
	
	
	

	public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
		super();
		this.jwtService = jwtService;
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
		final String role;
		
		
		// 2. header check
		if(authHeader==null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		
		jwt = authHeader.substring(7).trim(); // Bearer ke bad ka token
		
		try {
			// 3. extract username from token
			username = jwtService.extractUsername(jwt);
			role = jwtService.extractRole(jwt);
			
			// 4. agar username mila aur context empty hai
			
			if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
	
				UserDetails userDetails = User.withUsername(username)
				.password("")
				.authorities(authority)
				.build();
				
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
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.setContentType("application/json");
		    response.getWriter().write("""
		        {
		          "error": "JWT_EXPIRED",
		          "message": "Your session has expired. Please login again."
		        }
		    """);
		    return;
		}

		catch (UnsupportedJwtException | MalformedJwtException e) {
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.setContentType("application/json");
		    response.getWriter().write("""
		        {
		          "error": "INVALID_JWT",
		          "message": "Invalid authentication token."
		        }
		    """);
		    return;
		}

		catch (Exception e) {
		    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.setContentType("application/json");
		    response.getWriter().write("""
		        {
		          "error": "AUTH_ERROR",
		          "message": "Authentication failed."
		        }
		    """);
		    return;
		}
		
		
		filterChain.doFilter(request, response);
		
		
	}

}
