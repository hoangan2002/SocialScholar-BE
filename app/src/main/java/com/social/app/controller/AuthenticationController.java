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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

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
        System.out.println(userInfo);
       if(service.isExits(userInfo)) return  ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("User had been exits", "ERROR",null));
       return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Register success", "OK",service.addUser(userInfo)));
    }
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            if (((Collection<?>) authorities).stream().anyMatch(authority -> authority.toString().equals("ROLE_USER"))) {
                return "Welcome to User Profile";
            } else if (authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
                return "Welcome to Admin Profile";
            }

        }
        return "Access Denied";
    }



    @PostMapping("/sign-in")
    public ResponseEntity<ResponseObject> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            Optional<User> user = service.findUser(authRequest.getUserName());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","OK", AuthenticationResponse.builder()
                    .token(jwtService.generateToken(user))
                    .build()));
        } else {
           return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject("Login error", "Error",""));
        }
    }

    @PostMapping("/sign-in/admin")
    public ResponseEntity<ResponseObject> authenticateAndGetTokenADMIN(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            Optional<User> user = service.findUser(authRequest.getUserName());
            User userData = user.get();
            System.out.println(userData.getRole());
            if(!userData.getRole().contains("ADMIN"))  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject("Login error", "Error",""));
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK","OK", AuthenticationResponse.builder()
                    .token(jwtService.generateToken(user))
                    .build()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseObject("Login error", "Error",""));
        }
    }

}
