package com.social.app.controller;

import com.social.app.entity.GroupDTO;
import com.social.app.entity.PostResponse;
import com.social.app.entity.ResponseObject;
import com.social.app.model.Groups;

import com.social.app.model.JoinManagement;
import com.social.app.model.Post;
import com.social.app.model.User;
import com.social.app.repository.UserRepository;
import com.social.app.service.*;

import com.social.app.model.User;
import com.social.app.service.GroupServices;
import com.social.app.service.ImageStorageService;
import com.social.app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Calendar;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupServices groupServices;
    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    JoinService joinService;
    private final String FOLDER_PATH="D:\\New folder\\upload\\";



    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createGroup(@RequestBody Groups group, @RequestParam(value = "fileGAvatar", required = false) MultipartFile fileGAvatar, @RequestParam(value = "fileGCover", required = false) MultipartFile fileGCover){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            User user =  userService.findUser(username).orElse(null);
//them ngay tạo
            if(user.getActivityPoint() >= 2000){
                //set ava group

                if (fileGAvatar!=null) {

                    String fileName = imageStorageService.storeFile(fileGAvatar);

                    group.setImageURLGAvatar(FOLDER_PATH + fileName);

                }

                //set ảnh bìa group
                if (fileGCover!=null) {
                    String fileName = imageStorageService.storeFile(fileGCover);
                    group.setImageUrlGCover(FOLDER_PATH + fileName);
                }
                group.setHosts(user);
                userService.setRoleHost(user);
                //Thời gian tạo

                group.setTimeCreate(Calendar.getInstance().getTime());
                groupServices.createGroup(group);
                joinService.saveJoin(new JoinManagement(group,user,Calendar.getInstance().getTime()));

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Create Group Success", "OK", null));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Create Group Fail", "ERROR",null));
        } catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new ResponseObject("Create Group THROW", "ERROR",null ));}

    }
    @PutMapping("/update/{groupId}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<ResponseObject> updateGroup(@PathVariable Long groupId,@RequestBody Groups updatedGroup,@RequestParam(value = "fileGAvatar", required = false) MultipartFile fileGAvatar, @RequestParam(value = "fileGCover", required = false) MultipartFile fileGCover) {
        try {       if(groupServices.isGroupHost(groupId)) {
            Groups groups= groupServices.loadGroupById(groupId);
            MultipartFile[] file = {fileGAvatar , fileGCover};
            if(groups != null){
                if (updatedGroup.getDescription() !=null ){
                    groups.setDescription(updatedGroup.getDescription());
                }
                if (updatedGroup.getGroupName() !=null){
                    groups.setGroupName(updatedGroup.getGroupName());
                }
                if (updatedGroup.getCategory() != null){
                    groups.setCategory(updatedGroup.getCategory() );
                }
                if (updatedGroup.getHosts() != null){
                    groups.setHosts(updatedGroup.getHosts());
                }
                if (fileGAvatar!=null) {
                    String fileName = imageStorageService.storeFile(fileGAvatar);
                    groups.setImageURLGAvatar(FOLDER_PATH + fileName);
                }
                if (fileGCover!=null) {
                    String fileName = imageStorageService.storeFile(fileGCover);
                    groups.setImageUrlGCover(FOLDER_PATH + fileName);
                }
                return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Update Group Success", "OK",groupServices.updateGroup(groups)));
            }}
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Update Group Fail", "ERROR",null));
        }  catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new ResponseObject("Create Group THROW", "ERROR",null ));}}
    @PostMapping("/delete/{groupId}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<ResponseObject> deleteGroup(@PathVariable Long groupId) {
        if(groupServices.isGroupHost(groupId)) {
            Groups group = groupServices.loadGroupById(groupId);
            if (group != null) {
                groupServices.deleteGroup(groupId);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Delete Group Success", "OK", null));
            }
        }return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Delete Group Fail", "ERROR", null));
    }
    @GetMapping("/{groupId}")

//    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<ResponseObject> readGroup(@PathVariable String groupId) {
        System.out.println(groupId);
        try {
            Groups group = groupServices.loadGroupById(Integer.parseInt(groupId));
            if (group==null) {
                try {
                    int groupIdInt = Integer.parseInt(groupId);
                    group = groupServices.loadGroupByName(groupId);
                } catch (NumberFormatException ex) {
                    throw new Exception("Invalid groupId format");
                }
            }
            if (group == null) {
                throw new Exception("Group not found"); // Ném ngoại lệ nếu group không tồn tại
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseObject("Successful", "OK", group));
        } catch (Exception e) {
            // Xử lý ngoại lệ GroupNotFoundException ở đây
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseObject("Fail", e.getMessage(), null));
        }
    }


    @PostMapping("/find-group")
    public ArrayList<Groups> findPost(@RequestBody String findContent){
        ArrayList<Groups> allGroup = groupServices.retriveGroupFromDB();
        ArrayList<Groups> findResult = new ArrayList<>();
        if(findContent!= null && findContent != "\s") {
            for (Groups p : allGroup) {
                if (p.getGroupName() != null && p.getGroupName().toLowerCase().contains(findContent.toLowerCase().trim())) {
                    findResult.add(p);
                }
            }
        }else {
            return null;
        }
        return findResult;
    }
    @PostMapping("/join-group/{groupId}")
    public  ResponseEntity<ResponseObject> joinGroup(@PathVariable Long groupId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(userService.isGroupMember(groupId) || groupServices.isGroupHost(groupId)){
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("User Joined Group", "ERROR",null));
        }

        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Join Success", "OK",joinService.saveJoin(new JoinManagement(groupServices.loadGroupById(groupId), userService.findUserByUsername(username), Calendar.getInstance().getTime()))));
    }
    @PostMapping("/exit-group/{groupId}")
    public  ResponseEntity<ResponseObject> exitGroup(@PathVariable Long groupId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(!userService.isGroupMember(groupId) || groupServices.isGroupHost(groupId)){
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("User not in Group/ User is Host", "ERROR",null));
        }
        joinService.deleteJoin(groupServices.loadGroupById(groupId),userService.findUserByUsername(username));
        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Exit Success", "OK",null));
    }
    @PostMapping("/kick-user/{groupId}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public  ResponseEntity<ResponseObject> kickUser(@PathVariable Long groupId,@RequestParam String userName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameHost = authentication.getName();
        if(!groupServices.isGroupHost(groupId)){
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("User is not Host of Group", "ERROR",null));
        }
        //cannot self-provoke
        if(groupServices.isGroupHost(groupId, userName)){
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Can't kick Host of Group", "ERROR",null));
        }
        joinService.deleteJoin(groupServices.loadGroupById(groupId),userService.findUserByUsername(userName));
        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Kick out Success", "OK", null));


    }

    @GetMapping("/get-all-group/{userId}")
    public ArrayList<GroupDTO> getAllGroupOfUser(@PathVariable int userId){
        User user= userService.loadUserById(userId);
        ArrayList<Groups> groups  = new ArrayList<>();
        for (JoinManagement join: user.getJoins()
        ) {
            groups.add(join.getGroup());
        }
        return  groupServices.groupsResponses(groups);
    }
    @GetMapping("/getAllGroup")
    public ArrayList<GroupDTO> getAllGroup(){
        ArrayList<Groups> groups  = groupServices.retriveGroupFromDB();
        return  groupServices.groupsResponses(groups);
    }

}
