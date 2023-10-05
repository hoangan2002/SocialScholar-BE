package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.model.User;
import com.social.app.service.ImageStorageService;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/myProfile")

public class ProfileController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private UserService service;

    @Autowired
    ImageStorageService imageStorageService;

    private final String FOLDER_PATH="F:\\CampSchoolar\\uploads\\";
    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> getUserProfile(@PathVariable("username") String username) {
        User theUser = service.findUserByUsername(username);
        return theUser!=null?
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",theUser))
                :ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Fail", "OK",null));
    }

//    @GetMapping("")
//    public ResponseEntity<ResponseObject> getUserProfile() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User theUser = service.findUserByUsername(authentication.getName());
//
//        return theUser!=null?
//                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",theUser))
//                :ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Fail", "OK",null));
//    }

    @PutMapping("/edit-username")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> editUsername(@RequestParam("name") String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theUser = service.findUserByUsername(authentication.getName());
        if(theUser!=null)
            if(!theUser.getUserName().equals(name))
                if(!service.existUserName(name)){
                    theUser.setUserName(name);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",service.save(theUser)));
                }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Edit Fail", "OK",null));
    }

    @PutMapping("/edit-phone")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> editPhone(@RequestParam("phone") String phone){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theUser = service.findUserByUsername(authentication.getName());
        if(theUser!=null)
            if(!theUser.getPhone().equals(phone))
                if(!service.existPhone(phone)){
                    theUser.setPhone(phone);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",service.save(theUser)));
                }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Edit Fail", "OK",null));
    }

    @PutMapping("/edit-password")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public  ResponseEntity<ResponseObject> editPassword( @RequestParam("old-pass") String oldPass, @RequestParam("new-pass") String newPass){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        // xac thuc mk
        Authentication authentication1 = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,oldPass));
        // neu mk dc xac thuc
        if (authentication.isAuthenticated()){
            // doi mk
            User theUser = service.findUserByUsername(userName);
            if(theUser!=null) {
                service.updatePassword(theUser.getEmail(),newPass);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK", newPass));
            }
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
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> editAvatar(@RequestParam(value = "file") MultipartFile file){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User theUser = service.findUserByUsername(authentication.getName());
            if(theUser!=null){
                if(file!=null&&!file.isEmpty()){
                    if (theUser.getAvatarURL()!=null)
                    {
                        // xoa avatar cu neu co trong uploads
                        String deletePath = imageStorageService.getUploadsPath()+theUser.getAvatarURL();
                        File deleteFile = new File(deletePath);
                        if(deleteFile.exists())
                            imageStorageService.deleteFile(imageStorageService.getUploadsPath()+theUser.getAvatarURL());
                    }
                    // add avatar
;
                    String filename = imageStorageService.storeFile(file);

                    theUser.setAvatarURL(filename);
                    service.save(theUser);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",imageStorageService.getUploadsPath()+theUser.getAvatarURL()));
                }
            }
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(e.getMessage(), "failed", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Fail", "OK",null));
    }

    @GetMapping("get-avatar")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> getAvatar(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theUser = service.findUserByUsername(authentication.getName());
        File file = new File(imageStorageService.getUploadsPath() + theUser.getAvatarURL());
        String encodstring = imageStorageService.encodeFileToBase64Binary(file);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",encodstring));
    }


}