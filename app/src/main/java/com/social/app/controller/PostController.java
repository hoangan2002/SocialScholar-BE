package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.model.Post;
import com.social.app.model.PostLike;
import com.social.app.model.User;
import com.social.app.repository.PostRepository;
import com.social.app.repository.UserRepository;
import com.social.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/postservices")
public class PostController {
    @Autowired
    PostServices postServices;
    @Autowired
    UserService userService;

    @Autowired
    GroupServices groupServices;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    LikeService likeService;

    private final String FOLDER_PATH="/Users/nguyenluongtai/Downloads/social-scholar--backend/uploads/";

    //______________________________________Make_post____________________________________________________//
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/posting")
    public ResponseEntity<ResponseObject> submitPost(@RequestPart Post body,
                                                     @RequestParam(value = "file", required = false) MultipartFile[] file,
                                                     @RequestParam("userid") int userid,
                                                     @RequestParam("groupid") int groupid){
        try {
            if (userService.loadUserById(userid) != null) {
                body.setUser(userService.loadUserById(userid));
                if (groupServices.loadGroupById(groupid) != null && userService.isGroupMember(userid,groupid)==true) {

                    body.setGroup(groupServices.loadGroupById(groupid));

                    if (file != null && !file[0].isEmpty()) {
                        String imagePath="";
                        for(int i=0; i<file.length;i++) {
                            String fileName = imageStorageService.storeFile(file[i]);
                            imagePath=imagePath + FOLDER_PATH + fileName+" ";
                        }
                        body.setImageURL(imagePath.trim());
                    }else body.setImageURL("");
                    postServices.submitPostToDB(body);
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "Post successfully", body));
                }
            }
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "There are problem..", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed", "Post Error", body));

    }
    //______________________________________Edit_post____________________________________________________//
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PutMapping("/editpost")
    public ResponseEntity<ResponseObject>  updateUser(@RequestPart Post postData,
                                                      @RequestParam("userid") int userid,
                                                      @RequestParam("postid") long postid,
                                                      //nhap vao vi tri anh can xoa. Example: anh thu 1 va 2 => imageRemove[1,2]
                                                      @RequestParam(value="imageRemove",required = false)int[] imageRemove,
                                                      //thêm ảnh nếu cần
                                                      @RequestParam(value = "file", required = false) MultipartFile[] file){
        try {
            boolean check = false;
            String newImageList = "";
            if (postServices.loadPostById(postid).getUser().getUserId() == userid) {
                postData.setPostId(postid);
                if(postServices.loadPostById(postid).getImageURL()!=null) {
                    String arr[] = postServices.loadPostById(postid).getImageURL().trim().replaceAll("\\s+", " ").split(" ");
                    ArrayList<String> imagesArraylist = new ArrayList<String>(Arrays.asList(arr));
                    if (imageRemove != null) {
                        for (int index : imageRemove) {
                            if (index >= arr.length)
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                        new ResponseObject("failed", "We have " + arr.length + " Images, can't find the remove index", ""));
                        }
                        for (int i = 0; i < imageRemove.length; i++) {
                            imagesArraylist.set(imageRemove[i], "");
                        }
                        for (int i = 0; i < imagesArraylist.size(); i++) {
                            newImageList = newImageList + imagesArraylist.get(i) + " ";
                        }
                        postData.setImageURL(newImageList.replaceAll("\\s+", " ").trim());
                        check = true;
                    }
                }


                if (file != null) {
                    String  imagePathUploadEdit="";
                    String temp;
                    if(postData.getImageURL() ==null) temp="";
                    else temp = postData.getImageURL();
                    if(check ) newImageList=temp+" ";
                    else  newImageList=postServices.loadPostById(postid).getImageURL();
                    for(int i=0; i<file.length;i++) {
                        String fileName = imageStorageService.storeFile(file[i]);
                        imagePathUploadEdit= imagePathUploadEdit + FOLDER_PATH + fileName+" ";

                    }
                    postData.setImageURL((newImageList+" "+imagePathUploadEdit).replaceAll("\\s+", " ").trim());
                }else if(imageRemove==null){
                    postData.setImageURL(postServices.loadPostById(postid).getImageURL());
                }


                Post result = postServices.editPostDB(postData);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Post Edit successfully", result));
            }
        }catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("failed", "There are problem..", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed", "Post Not Found", ""));
    }

    //______________________________________Get_post____________________________________________________//

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @GetMapping("/getPost")
    public ArrayList<Post> retrieveAllPost(){
        ArrayList<Post> result = postServices.retrivePostFromDB();
        return result;
    }

    //______________________________________Get_post_by_group__________________________________________________//

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @GetMapping("/getPost/{groupid}")
    public ArrayList<Post> retrieveAllPostByGroup(@PathVariable("groupid") long groupid){
        ArrayList<Post> result = postServices.retrivePostFromDBByGroup(groupid);
        return result;
    }
    //______________________________________Delete_post____________________________________________________//
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @DeleteMapping("/deletepost/{postId}")
    public  ResponseEntity<ResponseObject> deleteParticularPost(@PathVariable("postId")long postId,@RequestParam("userid")int userid){
        if(postServices.loadPostById(postId)!=null && postServices.isPostByUser(userid,postId)==true){
            postServices.deletePostDB(postId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Delete Succesfully","")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed","Can't find post to delete","")
        );
    }

    @PostMapping("/dislike/{postId}")
    public  ResponseEntity<ResponseObject> dislikePost(@PathVariable("postId")long postId, @RequestParam("userid")int userId){
        try {
            if (postServices.loadPostById(postId) != null) {
                Post post = postServices.loadPostById(postId);
                User user = userService.loadUserById(userId);
                PostLike postLike = likeService.createPostLike(post, user, (byte) -1);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("OK", "Like post succesfully", postLike)
                );
            }
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed","You don't have permission to delete this post ! ",""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed","Can't find post","")
        );
    }

    @PostMapping("/like/{postId}")
    public  ResponseEntity<ResponseObject> likePost(@PathVariable("postId")long postId, @RequestParam("userId")int userId){
        if(postServices.loadPostById(postId)!=null){
            Post post = postServices.loadPostById(postId);
            User user = userService.loadUserById(userId);
            PostLike postLike = likeService.createPostLike(post, user, (byte)1);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Like post succesfully",postLike)
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("Failed","Can't find post","")
        );
    }

    //______________________________________Get_Hot_Post____________________________________________________//
    @GetMapping("/hotpost")
    public ArrayList<Post> getallhotpost() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (((Collection<?>) authorities).stream().anyMatch(authority -> authority.toString().equals("ROLE_USER"))) {
                return postServices.printHotPost(true);
            }
        }
        return postServices.printHotPost(false);
    }


}
