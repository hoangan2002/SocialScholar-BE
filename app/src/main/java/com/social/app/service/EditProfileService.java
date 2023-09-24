package com.social.app.service;

import com.social.app.model.User;
import com.social.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
public class EditProfileService {
    @Autowired
    private UserRepository theUserRepository;


    public User updateProfile(User theUser){
        return theUserRepository.findById(theUser.getUserId()).map(user -> {
            user.setUserName(theUser.getUserName());
            user.setPhone(theUser.getPhone());
            return theUserRepository.save(user);
        }).orElseThrow(()-> new RuntimeException("Can not edit this profile - "+ theUser.getUserId()));
    }
}
