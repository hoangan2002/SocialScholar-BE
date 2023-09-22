//package com.social.app.controller;
//
//import com.social.app.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Optional;
//
//
//@Controller
//@RequestMapping("/dashboard")
//public class DashboardController {
//	@Autowired
//	UserRepository userRepo;
//	@GetMapping
//    public String displayDashboard(Model model){
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		if(securityContext.getAuthentication().getPrincipal() instanceof DefaultOAuth2User) {
//		DefaultOAuth2User user = (DefaultOAuth2User) securityContext.getAuthentication().getPrincipal();
//		model.addAttribute("userDetails", user.getAttribute("name")!= null ?user.getAttribute("name"):user.getAttribute("login"));
//		}else {
//				User user = (User) securityContext.getAuthentication().getPrincipal();
//				Optional<com.social.app.model.User> userOptional = userRepo.findByEmail(user.getUsername());
//				com.social.app.model.User users = userOptional.orElse(null);
//			model.addAttribute("userDetails", users.getUserName());
//		}
//        return "dashboard";
//    }
//
//}
