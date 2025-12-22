package com.rk.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.rk.entity.Gender;
import com.rk.entity.Role;
import com.rk.entity.User;
import com.rk.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	private final JwtService jwtService;
	private final UserRepository userRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		
		OAuth2User oauthUser =(OAuth2User) authentication.getPrincipal();
		String email = oauthUser.getAttribute("email");
		String name = oauthUser.getAttribute("name");
		 String imageUrl = oauthUser.getAttribute("picture");
		 
		
		// find or create user
		User user = userRepository.findByEmail(email)
				.orElseGet(()->{
					User newUser = User.builder()
							.email(email)
							.fullName(name)
							.role(Role.STUDENT)
							.password("")
							.gender(Gender.NOTDEFINE)
							.isActive(true)
							.imageUrl(imageUrl)
							.build();
					
					return userRepository.save(newUser);
				});
		
		UserDetails userDetails = new CustomUserDetails(user);
		// Generate JWT
		String jwt = jwtService.generateToken(userDetails);
		
		// redirect JWT to frontend 
		String redirectUrl = "https://hostelhub.online/oauth-success?token=" + jwt;	
		getRedirectStrategy().sendRedirect(request, response, redirectUrl);
				}
	}

