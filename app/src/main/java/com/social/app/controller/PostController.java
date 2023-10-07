package com.social.app.controller;

import com.social.app.dto.PostReportDTO;
import com.social.app.entity.PostResponse;
import com.social.app.entity.ResponseObject;
import com.social.app.model.*;
import com.social.app.repository.PostRepository;
import com.social.app.repository.UserRepository;
import com.social.app.request.PostDTO;
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
@RequestMapping("/api/poster")
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

    @Autowired
    ReportService reportService;

    @Autowired
    ResponseConvertService responseConvertService;


    private final String FOLDER_PATH="/Users/nguyenluongtai/Downloads/social-scholar--backend/uploads/";

    //______________________________________Make_post____________________________________________________//
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/posting")
    public ResponseEntity<ResponseObject> submitPost(@RequestPart PostDTO body){
        Post post = new Post();
        String content ;
        Timestamp time;
        int userId;
        int groupId;
        String title ;
        // Check if user is not in group, user can not dislike post
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
        if(!userService.isGroupMember(body.getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed","User must be in group","")
            );
        try {
            if (userService.loadUserById(userid) != null) {
                post.setUser(userService.loadUserById(userid));
                if (groupServices.loadGroupById(body.getGroupId()) != null) {

                    post.setGroup(groupServices.loadGroupById(body.getGroupId()));
                    post.setTime(body.getTime());

//                    if (file != null && !file[0].isEmpty()) {
//                        String imagePath="";
//                        for(int i=0; i<file.length;i++) {
//                            String fileName = imageStorageService.storeFile(file[i]);
//                            imagePath=imagePath + FOLDER_PATH + fileName+" ";
//                        }
//                        body.setImageURL(imagePath.trim());
//                    }else {
//                        body.setImageURL("");
//                    }
                    postServices.submitPostToDB(post);

                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("ok", "Post successfully", post));
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
//                                                      @RequestParam("userid") int userid,
                                                      @RequestParam("postid") long postid,
                                                      //nhap vao vi tri anh can xoa. Example: anh thu 1 va 2 => imageRemove[1,2]
                                                      @RequestParam(value="imageRemove",required = false)int[] imageRemove,
                                                      //thêm ảnh nếu cần
                                                      @RequestParam(value = "file", required = false) MultipartFile[] file){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
        try {
            boolean check = false;

            if (postServices.loadPostById(postid).getUser().getUserId() == userid) {
                postData.setPostId(postid);
                String arr[] = postServices.loadPostById(postid).getImageURL().trim().replaceAll("\\s+", " ").split(" ");
                ArrayList<String> imagesArraylist = new ArrayList<String>(Arrays.asList(arr));
                String newImageList="";
                if(imageRemove!= null){
                    for(int index:imageRemove){
                        if(index>=arr.length)
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                    new ResponseObject("failed", "We have "+arr.length+" Images, can't find the remove index", ""));
                    }

                    for(int i=0; i<imageRemove.length;i++){
                        imagesArraylist.set(imageRemove[i],"");
                    }

                    for(int i=0; i<imagesArraylist.size();i++){
                        newImageList= newImageList + imagesArraylist.get(i)+" ";
                    }
                    postData.setImageURL(newImageList.replaceAll("\\s+", " ").trim());
                    check=true;
                }

                if (file != null) {
                    String  imagePathUploadEdit="";

                    if(check) newImageList=postData.getImageURL()+" ";
                    else  newImageList=postServices.loadPostById(postid).getImageURL().trim();

                    for(int i=0; i<file.length;i++) {
                        String fileName = imageStorageService.storeFile(file[i]);
                        imagePathUploadEdit= imagePathUploadEdit + fileName+" ";

                    }
                    postData.setImageURL((newImageList+" "+imagePathUploadEdit).replaceAll("\\s+", " ").trim());
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

    //    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
//    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @GetMapping("/getPosts")
    public ArrayList<PostResponse> retrieveAllPost(){
        ArrayList<Post> result = postServices.retrivePostFromDB();
        return responseConvertService.postResponseArrayList(result);
    }
    //______________________________________Delete_post____________________________________________________//
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @DeleteMapping("/deletepost/{postId}")
    public  ResponseEntity<ResponseObject> deleteParticularPost(@PathVariable("postId")long postId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
        if(postServices.loadPostById(postId)!=null && postServices.loadPostById(postId).getUser().getUserName().matches(authentication.getName())){
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
    public  ResponseEntity<ResponseObject> dislikePost(@PathVariable("postId")long postId, @RequestParam("user")String userName){
        User current = userService.findUserByUsername(userName);
        int userId = current != null ?current.getUserId():-1;
        // check if post is not found, return
        if (postServices.loadPostById(postId)==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed","Can't find post","")
            );

        Post post = postServices.loadPostById(postId);
        // Check if user is not in group, user can not dislike post
        if(!userService.isGroupMember( post.getGroup().getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed","User must be in group","")
            );

        User user = userService.loadUserById(userId);
        // check if user already dislike, delete postlike
        if(likeService.getPostLike(postId,userId)!=null){
            // call delete function
            likeService.deletePostLike(likeService.getPostLike(postId,userId));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Like post successfully","")
            );
        }

        // else create postlike
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Dislike post successfully",likeService.createPostLike(post, user, (byte)-1))
        );
    }

    @PostMapping("/like/{postId}")
    public  ResponseEntity<ResponseObject> likePost(@PathVariable("postId")long postId, @RequestParam("user")String userName){
        // check if post is not found, return
        User current = userService.findUserByUsername(userName);
        int userId = current != null ?current.getUserId():-1;
        System.out.println(userId); 
        if (postServices.loadPostById(postId)==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed","Can't find post","")
            );

        Post post = postServices.loadPostById(postId);
        // Check if user is not in group, user can not like post
        if(!userService.isGroupMember( post.getGroup().getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed","User must be in group","")
            );

        User user = userService.loadUserById(userId);
        // check if user already dislike, delete postlike
        if(likeService.getPostLike(postId,userId)!=null){
            // call delete function
            likeService.deletePostLike(likeService.getPostLike(postId,userId));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Like post successfully","")
            );
        }

        // else create postlike
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Like post successfully",likeService.createPostLike(post, user, (byte)1))
        );
    }

    @GetMapping("/getLike/{postId}")
    public int getPostLike(@PathVariable long postId){
        return likeService.getTotalPostLike(postId);
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

    @PostMapping("/report/{postId}")
    public  ResponseEntity<ResponseObject> reportPost(@PathVariable long postId, @RequestParam("userid")int userId,
                                                      @RequestParam("typeid") int typeId, @RequestParam("description") String description){
        // check if post is not found, return
        if (postServices.loadPostById(postId)==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed","Can't find post","")
            );

        Post post = postServices.loadPostById(postId);
        // Check if user is not in group, user can not report post
        if(!userService.isGroupMember(post.getGroup().getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed","User must be in group","")
            );

        // set description to report
        PostReport postReport = new PostReport();
        postReport.setDescription(description);

        // Create user
        User user = userService.loadUserById(userId);

        // create postreport
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Report post successfully",reportService.createPostReport(post, user, postReport, typeId))
        );
    }

    @GetMapping("/all-reports/{postId}")
    public ArrayList<PostReport> getAllReportsByPostId(@PathVariable long postId){
        return reportService.getAllPostReportsByPostId(postId);
    }

    @GetMapping("/all-reports")
    public ArrayList<PostReportDTO> getAllReports(){
        return reportService.getAllPostReports();
    }

    @GetMapping("/all-report-types")
    public ArrayList<PostReportType> getAllReportTypes(){
        return reportService.getAllPostReportTypes();
    }

    @GetMapping("/find-post")
    public ArrayList<PostResponse> findPost(@RequestParam("findContent") String findContent){
        ArrayList<Post> allPost = postServices.retrivePostFromDB();
        ArrayList<Post> findResult = new ArrayList<>();
        if(findContent!= null && findContent != "\s") {
            for (Post p : allPost) {
                if (p.getTitles()!= null && p.getTitles().toLowerCase().contains(findContent.toLowerCase().trim())) {
                    findResult.add(p);
                }
            }
        }else {
            return null;
        }
        return responseConvertService.postResponseArrayList(findResult);
    }
    @GetMapping("/find-post/{groupId}")
    public ArrayList<PostResponse> findPostGroup(@PathVariable Long groupId,@RequestParam("findContent") String findContent){
        ArrayList<Post> allPostGroup = postServices.retriveGroupPostFromDB(groupId);
        ArrayList<Post> findResultGroup = new ArrayList<>();
        if(findContent!= null && findContent != "\s") {
            for (Post p : allPostGroup) {
                if (p.getTitles()!= null && p.getTitles().toLowerCase().contains(findContent.toLowerCase().trim())) {
                    findResultGroup.add(p);
                }
            }
        }else {
            return null;
        }
        return responseConvertService.postResponseArrayList(findResultGroup);
    }
    @PostMapping("/donate/{postid}")
    public ResponseEntity<ResponseObject> donate(@PathVariable long postid,@RequestParam long coins){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
        User user = userService.loadUserById(userid);
        Post post = postServices.loadPostById(postid);
        if(user.getCoin()>coins && postServices.loadPostById(postid).getUser()!= user){
            user.setCoin(user.getCoin()-coins);
            userService.save(user);
            post.getUser().setCoin(post.getUser().getCoin()+coins);
            userService.save(post.getUser());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Donate success","")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("Failed","Donate failed","")
        );
    }

    @GetMapping("/getPostDTO")
    public ArrayList<com.social.app.dto.PostDTO> retrieveAllPostDTO(){
        ArrayList<Post> result = postServices.retrivePostFromDB();
        return postServices.ArrayListPostDTO(result);
    }

    @GetMapping("/getPostDTObylike")
    public ArrayList<com.social.app.dto.PostDTO> retrieveAllPostDTOByLike(){
        ArrayList<Post> result = postServices.getAllPostByLike();
        return postServices.ArrayListPostDTO(result);
    }

    @GetMapping("/getPostDTObycomment")
    public ArrayList<com.social.app.dto.PostDTO> retrieveAllPostDTOByComment(){
        ArrayList<Post> result = postServices.getAllPostByComment();
        return postServices.ArrayListPostDTO(result);
    }

    @GetMapping("/getPostDTObytime")
    public ArrayList<com.social.app.dto.PostDTO> retrieveAllPostDTOByTime(){
        ArrayList<Post> result = postServices.getAllPostByTime();
        return postServices.ArrayListPostDTO(result);
    }



}