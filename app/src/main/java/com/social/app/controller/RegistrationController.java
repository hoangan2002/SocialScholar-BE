//package com.social.app.controller;
//
//import com.social.app.model.User;
//import com.social.app.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/registration")
//public class RegistrationController {
//
//	@Autowired
//	 private UserService userService;
//
//
//	    @ModelAttribute("user")
//	    public User userRegistrationDto() {
//	        return new User();
//	    }
//
//	    @GetMapping
//	    public String showRegistrationForm() {
//	        return "register";
//	    }
//
//	    @PostMapping
//	    public String registerUserAccount(@ModelAttribute("user")
//	              User registrationDto) {
//			System.out.println(registrationDto);
//	        userService.addUser(registrationDto);
//	        return "redirect:/login";
//	    }
//}
