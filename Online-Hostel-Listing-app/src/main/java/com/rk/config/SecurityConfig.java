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
	private final OAuthSuccessHandler authSuccessHandler;

	@Bean
	PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, PasswordEncoder encoder) throws Exception {
		return http.csrf(csrf -> csrf.disable())

				.authorizeHttpRequests(auth -> auth
					    .requestMatchers("/", "/api/auth/**", "/api/common/**",
					        "/api/hostels/**", "/api/payment/**",
					        "/login/oauth2/**", "/oauth2/**").permitAll()
					    .requestMatchers("/api/admin/**").hasAuthority("OWNER")
					    .requestMatchers("/api/iam/**").hasAuthority("IAM")
					    .requestMatchers("/api/student/**").hasAuthority("STUDENT")
					    .anyRequest().authenticated())
				 .oauth2Login(oauth -> oauth
						    .successHandler(authSuccessHandler)
						)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.build();

	}
	private CorsConfigurationSource corsConfigurationSource() {
		return new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration cfg = new CorsConfiguration();
				cfg.setAllowedOrigins(Arrays.asList("http://localhost:5173","http://192.168.0.19:5173","http://192.168.43.110:5173", "https://hostelhub.online", "https://www.hostelhub.online"));
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
