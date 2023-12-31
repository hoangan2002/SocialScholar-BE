package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.model.User;
import com.social.app.service.ImageStorageService;
import com.social.app.service.UserSalerReportServices;
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
    UserSalerReportServices userSalerReportServices;

    @Autowired
    private UserService service;

    @Autowired
    ImageStorageService imageStorageService;

    private final String FOLDER_PATH="F:\\CampSchoolar\\uploads\\";
    @PostMapping("/{username}")
   @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> getUserProfile(@PathVariable("username") String username) {
        User theUser = service.findUserByUsername(username);
        return theUser!=null?
                ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",service.MapUserDTO(theUser)))
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
                    User result = service.save(theUser);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",result.getUserName()));
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
                    User result = service.save(theUser);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",result.getPhone()));
                }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Edit Fail", "OK",null));
    }

    @PutMapping("/bannedOrNot")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> bannedUser(@RequestParam("userId") int userId){
        User theUser = service.findById(userId);
        if(theUser!=null)
            {
                boolean state = theUser.isLocked();
                System.out.println("locked"+state);
               theUser.setLocked(!state);
               service.save(theUser);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Edit Oke", "OK",null));
            }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Edit Fail", "Fail",null));
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

    @PostMapping("/edit-avatar")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> editAvatar(@RequestBody User user){
        System.out.println(user.getAvatarURL());
        System.out.println(user.getBackgroundURL());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User theUser = service.findUserByUsername(authentication.getName());
            if(theUser!=null){
                if(user.getAvatarURL() != null) theUser.setAvatarURL(user.getAvatarURL());
                if(user.getBackgroundURL() != null) theUser.setBackgroundURL(user.getBackgroundURL());
                if(user.getPhone() != null) theUser.setPhone(user.getPhone());
                service.save(theUser);
            }
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject(e.getMessage(), "failed", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject("Fail", "OK",null));
    }

//    @GetMapping("get-avatar")
//    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
//    public ResponseEntity<ResponseObject> getAvatar(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User theUser = service.findUserByUsername(authentication.getName());
//        File file = new File(imageStorageService.getUploadsPath() + theUser.getAvatarURL());
//        String encodstring = imageStorageService.encodeFileToBase64Binary(file);
//        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",encodstring));
//    }

    @GetMapping("/get-avatar")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> getAvatar(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theUser = service.findUserByUsername(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",theUser.getAvatarURL()));
    }

    @PostMapping("/getPoint")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> getPoint(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theUser = service.findUserByUsername(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",theUser.getActivityPoint()));
    }

    @PostMapping("/getCoint")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> getCoint(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("data la"+authentication.getName());
        User theUser = service.findUserByUsername(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",theUser.getCoin()));
    }

    @GetMapping("/get-sale-report")
    @PreAuthorize("isAuthenticated() and hasAuthority('ROLE_USER')")
    public ResponseEntity<ResponseObject> getSaleReport(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User theUser = service.findUserByUsername(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject( "Successful", "OK",userSalerReportServices.saleReport(theUser.getUserId())));
    }

}