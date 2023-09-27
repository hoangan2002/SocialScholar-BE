package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.model.Groups;
import com.social.app.model.User;
import com.social.app.repository.UserRepository;
import com.social.app.service.GroupServices;
import com.social.app.service.UserInfoDetails;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupServices groupServices;
    @Autowired
    UserService userService;




    @GetMapping
    public ResponseEntity<ResponseObject> createGroup(@RequestBody Groups group){
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "hoangan2002";//authentication.getName();
        User user =  userService.findUser(username).orElse(null);

        if(user.getActivityPoint() >= 2000){
            userService.setRoleHost(user);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Create Group Success", "OK", groupServices.createGroup(group)));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Create Group Fail", "ERROR",null));
    }
    @PutMapping("/update/{groupId}")
   @PreAuthorize("hasAnyRole('ROLE_HOST')")

    public ResponseEntity<ResponseObject> updateGroup(@PathVariable Long groupId,@RequestBody Groups updatedGroup) {
           Groups groups= groupServices.loadGroupById(groupId);
           if(groups != null){
               if (updatedGroup.getDescription() != null){
                   groups.setDescription(updatedGroup.getDescription());
               }
               if (updatedGroup.getGroupName() != null){
                   groups.setGroupName(updatedGroup.getGroupName());
               }
               if (updatedGroup.getCategory() != null){
                   groups.setCategory(updatedGroup.getCategory() );
               }
               if (updatedGroup.getHosts() != null){
                   groups.setHosts(updatedGroup.getHosts());
               }
               return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Update Group Success", "OK",groupServices.updateGroup(groups)));
           }
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Update Group Fail", "ERROR",null));
    }
    @DeleteMapping("/delete/{groupId}")
    public ResponseEntity<ResponseObject> deleteGroup(@PathVariable Long groupId) {
        Groups group = groupServices.loadGroupById(groupId);
        if (group != null) {
            groupServices.deleteGroup(groupId);
            return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Delete Group Success", "OK",null));
        } else {
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Delete Group Fail", "ERROR",null));
        }
    }
    @GetMapping("/read/{groupId}")
    public  ResponseEntity<ResponseObject> readGroup(@PathVariable Long groupId){
        Groups group = groupServices.loadGroupById(groupId);
        if (group != null) {

            return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Delete Group Success", "OK",group));
        } else {
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Delete Group Fail", "ERROR",null));
        }

    }


}
