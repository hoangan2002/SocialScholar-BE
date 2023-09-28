package com.social.app.config;

import com.social.app.model.User;
import com.social.app.repository.UserRepository;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler{

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService userService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String redirectUrl = null;
		if(authentication.getPrincipal() instanceof DefaultOAuth2User) {
		DefaultOAuth2User  userDetails = (DefaultOAuth2User ) authentication.getPrincipal();

         String username = userDetails.getAttribute("email") !=null?userDetails.getAttribute("email"):userDetails.getAttribute("login")+"@gmail.com" ;
			System.out.println(username);
          if(userRepo.findByEmail(username).isEmpty()) {
        	  User user = new User();
        	  user.setEmail(username);
        	  user.setUserName(userDetails.getAttribute("email") !=null?userDetails.getAttribute("name"):userDetails.getAttribute("login"));
        	  user.setPassword("Dummy");
        	  userService.addUser(user);
          }
		}  redirectUrl = "/home";
		System.out.println("login oke");
		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}

}
