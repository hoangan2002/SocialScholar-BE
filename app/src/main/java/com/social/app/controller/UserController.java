package com.social.app.controller;

import com.social.app.entity.AuthRequest;
import com.social.app.entity.AuthenticationResponse;
import com.social.app.entity.ResponseObject;
import com.social.app.model.User;
import com.social.app.service.JwtService;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseObject> register(@RequestBody User userInfo) {
       if(service.isExits(userInfo)) return  ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("User had been exits", "ERROR",null));
       return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Register success", "OK",service.addUser(userInfo)));
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public ResponseEntity<ResponseObject> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","OK", AuthenticationResponse.builder()
                    .token(jwtService.generateToken(authRequest.getUserName()))
                    .build()));
        } else {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject("Login error", "Error",""));
        }
    }

}