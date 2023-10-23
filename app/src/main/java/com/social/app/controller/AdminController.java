package com.social.app.controller;

import com.social.app.dto.GroupDTO;
import com.social.app.dto.UserDTO;
import com.social.app.entity.UserResponse;
import com.social.app.model.Groups;
import com.social.app.model.JoinManagement;
import com.social.app.model.User;
import com.social.app.service.GroupServices;
import com.social.app.service.ResponseConvertService;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    ResponseConvertService responseConvertService;
    @Autowired
    GroupServices groupServices;
    @PostMapping("/getalluser")
    @PreAuthorize("hasRole('ADMIN')")
    public ArrayList<UserDTO> getalluser(){
        ArrayList<User> listUser = userService.findAll();
        return  userService.userResponses(listUser);
    }


    @GetMapping("/getallgroup/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ArrayList<GroupDTO> getAllGroupOfUser(@PathVariable int userId){
       User user= userService.loadUserById(userId);
       ArrayList<Groups> groups  = new ArrayList<>();
        for (JoinManagement join: user.getJoins()
             ) {
            groups.add(join.getGroup());
        }
       return  groupServices.groupsResponses(groups);
    }
}
