package com.social.app.controller;


import com.social.app.dto.GroupDTO;
import com.social.app.dto.UserDTO;
import com.social.app.entity.CountResponse;
import com.social.app.entity.Data;

import com.social.app.dto.*;
import com.social.app.entity.ResponseObject;

import com.social.app.entity.UserResponse;
import com.social.app.model.Groups;
import com.social.app.model.JoinManagement;
import com.social.app.model.User;

import com.social.app.service.GroupServices;
import com.social.app.service.ResponseConvertService;
import com.social.app.service.UserService;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.social.app.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

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

    //Theo dõi số lượng người dùng mới
    @GetMapping("/statistics-user")
    @PreAuthorize("hasRole('ADMIN')")
    public CountResponse statisticsUsers() {
        ArrayList<User> users = userService.findAll();

        Date startDate = userService.findFirstUser().getTimeCreate();

        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(currentDate);
        ArrayList<String> dates = new ArrayList<>();
        String counts = "[";

        while (!calendar.after(endCalendar)) {
            Date currentDay = calendar.getTime();
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = outputFormat.format(currentDay.getTime());
            dates.add(formattedDate);
            int count = 0;
            for (User user : users) {
                if (user.getTimeCreate().getYear() == currentDay.getYear() &&
                        user.getTimeCreate().getMonth() == currentDay.getMonth() &&
                        user.getTimeCreate().getDate() == currentDay.getDate()) {
                    count++;
                }
            }


            calendar.add(Calendar.DATE, 1); // Tăng ngày lên 1

            if (calendar.after(endCalendar)) {
                counts = counts + count;
            } else {
                counts = counts + count + ",";
            }
        }
        counts = counts + "]";
        CountResponse countResponse = new CountResponse(dates, new Data("userAccount", counts));
        return countResponse;

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
