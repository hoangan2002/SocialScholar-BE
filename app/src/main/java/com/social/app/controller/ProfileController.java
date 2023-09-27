package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.model.Groups;
import com.social.app.model.User;
import com.social.app.repository.UserRepository;
import com.social.app.service.EditProfileService;
import com.social.app.service.ImageStorageService;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RestController
@RequestMapping("/myProfile")
//@PreAuthorize("hasAuthority('ROLE_USER')")
public class ProfileController {
    @Autowired
    private UserService service;

    @Autowired
    ImageStorageService imageStorageService;
    private final String FOLDER_PATH="F:\\CampSchoolar\\uploads\\";
    @GetMapping("{userId}")
    public ResponseEntity<ResponseObject> getUserProfile(@PathVariable int userId) {
        User theUser = service.findById(userId);
        return theUser!=null?
        ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",theUser))
        :ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Fail", "OK",null));
    }

    @PutMapping("/edit-username")
    public ResponseEntity<ResponseObject> editUsername(@RequestParam("id") int id, @RequestParam("name") String name){
        User theUser = service.findById(id);
        if(theUser!=null)
            if(!theUser.getUserName().equals(name))
                if(!service.existUserName(name)){
                    theUser.setUserName(name);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",service.save(theUser)));
                }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Edit Fail", "OK",null));
    }

    @PutMapping("/edit-phone")
    public ResponseEntity<ResponseObject> editPhone(@RequestParam("id") int id, @RequestParam("phone") String phone){
        User theUser = service.findById(id);
        if(theUser!=null)
            if(!theUser.getPhone().equals(phone))
                if(!service.existPhone(phone)){
                    theUser.setPhone(phone);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",service.save(theUser)));
                }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Edit Fail", "OK",null));
    }

    @PutMapping("/edit-password")
    public  ResponseEntity<ResponseObject> editPassword(@RequestParam("id") int id, @RequestParam("pass") String pass){
        // chưa validate password
        User theUser = service.findById(id);
        if(theUser!=null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",service.updatePassword(theUser.getEmail(), theUser.getPassword())));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Fail", "OK",null));
    }

//    @PostMapping("/add-avatar")
//    public ResponseEntity<ResponseObject> addAvatar(@RequestParam("id") int id, @RequestParam(value = "file") MultipartFile file){
//        try{
//            User theUser = service.findById(id);
//            if(theUser!=null){
//                if(file!=null&&!file.isEmpty()){
//                    String filename = imageStorageService.storeFile(file);
//                    String imagePath = FOLDER_PATH + filename;
//                    theUser.setAvatarURL(imagePath);
//                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",service.save(theUser)));
//                }
//            }
//        }catch (RuntimeException e){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    new ResponseObject(e.getMessage(), "failed", ""));
//        }
//        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Fail", "OK",null));
//    }

    @PutMapping("/edit-avatar")
    public ResponseEntity<ResponseObject> editAvatar(@RequestParam("id") int id, @RequestParam(value = "file") MultipartFile file){
        try {
            User theUser = service.findById(id);
            if(theUser!=null){
                if(file!=null&&!file.isEmpty()){
                    if (theUser.getAvatarURL()!=null || !theUser.getAvatarURL().isEmpty())
                    {
                        // xoa avatar cu neu co trong uploads
                        imageStorageService.deleteFile(theUser.getAvatarURL());
                    }
                    // add avatar
                    String filename = imageStorageService.storeFile(file);
                    String imagePath = FOLDER_PATH + filename;
                    theUser.setAvatarURL(imagePath);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",null));
                }
            }
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(e.getMessage(), "failed", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Fail", "OK",null));
    }
}