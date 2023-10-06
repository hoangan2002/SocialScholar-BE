package com.social.app.controller;

import com.social.app.entity.UserResponse;
import com.social.app.model.User;
import com.social.app.service.ResponseConvertService;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    ResponseConvertService responseConvertService;
    @GetMapping("/getalluser")
    @PreAuthorize("hasRole('ADMIN')")
    public ArrayList<UserResponse> getalluser(){
        ArrayList<User> listUser = userService.findAll();
        return  responseConvertService.userResponseArrayList(listUser);
    }
}
