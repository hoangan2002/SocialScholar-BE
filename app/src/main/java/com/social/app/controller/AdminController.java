package com.social.app.controller;

import com.social.app.dto.*;
import com.social.app.entity.ResponseObject;
import com.social.app.entity.UserResponse;
import com.social.app.model.Groups;
import com.social.app.model.JoinManagement;
import com.social.app.model.User;
import com.social.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    ResponseConvertService responseConvertService;
    @Autowired
    GroupServices groupServices;
    @Autowired
    PostServices postServices;
    @Autowired
    private CommentService commentService;
    @GetMapping("/getalluser")
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

    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable String username){
        try {
            userService.deleteUser(username);
            return ResponseEntity.ok().body(
                    new ResponseObject("delete successfully", "OK", username)
            );
        }catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("delete fail", "Fail", ex.getMessage())
            );
        }
    }
    @GetMapping("/get-reported-comments")
    public ArrayList<ReportedCommentDTO> getReportedComments(){
        return commentService.getAllReportedComment();
    }
    @GetMapping("/get-reported-posts")
    public ArrayList<ReportedPostDTO> getReportedPosts(){
        return postServices.getAllReportedPost();
    }
    @GetMapping("/get-group-count")
    public HashMap<CategoryDTO, Integer> getGroupCount(){
        return groupServices.getGroupCount();
    }
}
