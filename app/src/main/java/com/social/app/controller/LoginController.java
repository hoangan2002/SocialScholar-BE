//package com.social.app.controller;
//
//import com.social.app.entity.AuthRequest;
//import com.social.app.repository.UserRepository;
//import com.social.app.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//
//
//@Controller
//@RequestMapping("/login")
//public class LoginController {
//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	UserRepository userRepo;
//
//    @ModelAttribute("user")
//    public AuthRequest userLoginDTO() {
//        return new AuthRequest();
//    }
//
//	@GetMapping
//	public String login() {
//		return "login";
//	}
//
//	@PostMapping
//	public void loginUser(@ModelAttribute("user")
//	 AuthRequest authRequest) {
//		System.out.println("UserDTO"+authRequest);
//		 userService.loadUserByUsername(authRequest.getUserName());
//	}
//
//}
