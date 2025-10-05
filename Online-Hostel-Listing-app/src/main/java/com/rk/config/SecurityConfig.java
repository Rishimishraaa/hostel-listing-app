package com.rk.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;

@Configuration
@Data
public class SecurityConfig {
	
	
	private final CustomUserDetailsService userDetailsService;
	private final JwtFilter jwtFilter;

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationProvider authenticationProvider(PasswordEncoder encoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(encoder);
		return provider;
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http,PasswordEncoder encoder) throws Exception {
		return http.csrf(csrf->csrf.disable())
				
				.authorizeHttpRequests(auth->
		auth.requestMatchers("/api/auth/**","/api/common/**","/api/hostels/**","/api/payment/**").permitAll()
			.requestMatchers("/api/admin/**").hasAuthority("OWNER")
			.requestMatchers("/api/student/**").hasAuthority("STUDENT")
			.anyRequest().authenticated()
				)
				.authenticationProvider(authenticationProvider(encoder))
				
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
				.cors(cors->cors.configurationSource(corsConfigurationSource()))
				.build();
		
	}
	
	  private CorsConfigurationSource corsConfigurationSource() {
	        return new CorsConfigurationSource() {
	            @Override
	            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	                CorsConfiguration cfg = new CorsConfiguration();
	                cfg.setAllowedOrigins(Arrays.asList(
	                        "http://localhost:3000","http://localhost:5173",
	                        "http://192.168.43.110:5173",
	                        "http://192.168.1.3:5173",
	                        "http://192.168.1.11:5173"
	                       
	                 
	                     
	                ));
	                cfg.setAllowedMethods(Collections.singletonList("*"));
	                cfg.setAllowCredentials(true);
	                cfg.setAllowedHeaders(Collections.singletonList("*"));
	                cfg.setExposedHeaders(Arrays.asList("Authorization"));
	                cfg.setMaxAge(360L);
	                return cfg;
	            }
	        };
	    }

}
