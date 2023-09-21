package com.social.app.controller;

import com.social.app.model.User;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private UserService theUserService;

    @GetMapping("/{userId}")
    public User getUserProfile(@PathVariable int userId) {
        return theUserService.findById(userId);
    }

    @PutMapping("/edit")
    public User editUserProfile(@RequestBody User theUser){
        User dbUserId = theUserService.findById(theUser.getUserId());
        String theUserName = theUser.getUserName();
        if(!dbUserId.getUserName().equals(theUserName)){
            if(theUserService.existUserName(theUserName))
                throw new RuntimeException("The Username was existed");
            else return theUserService.save(theUser);
        }
        else return theUserService.save(theUser);
    }
}
