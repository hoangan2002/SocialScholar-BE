package com.social.app.controller;

import com.social.app.model.User;
import com.social.app.repository.UserRepository;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService theUserService;

    @Autowired
    private UserRepository theUserRepository;

    @GetMapping("/{userId}")
    public User getUserProfile(@PathVariable int userId) {
        return theUserService.findById(userId);
    }

    @PutMapping("/edit")
    public User editUserProfile(@RequestBody User theUser){
        return theUserRepository.findById(theUser.getUserId()).map(user -> {
            user.setUserName(theUser.getUserName());
            user.setPhone(theUser.getPhone());
            return theUserRepository.save(user);
        }).orElseThrow(()-> new RuntimeException("Can not edit this profile - "+ theUser.getUserId()));
    }
}
