package com.social.app.controller;

import com.social.app.dto.PostReportDTO;
import com.social.app.dto.PostReportTypeDTO;
import com.social.app.dto.ReportedPostDTO;
import com.social.app.entity.PostResponse;
import com.social.app.entity.ResponseObject;
import com.social.app.model.*;
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

    @Autowired
    CommentService commentService;
    private final String FOLDER_PATH="/Users/nguyenluongtai/Downloads/social-scholar--backend/uploads/";

    //______________________________________Make_post____________________________________________________//
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/posting")
    public ResponseEntity<ResponseObject> submitPost(@RequestBody PostDTO body){
        Post post = new Post();
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
                    post.setContent(body.getContent());

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
    @PostMapping("/editpost")
    public ResponseEntity<ResponseObject>  updateUser(@RequestBody PostDTO postData,
//                                                      @RequestParam("userid") int userid,
                                                      @RequestParam("postid") long postid
                                                      //nhap vao vi tri anh can xoa. Example: anh thu 1 va 2 => imageRemove[1,2]

                                                      //thêm ảnh nếu cần
                                                     ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
//        try {
//            boolean check = false;
//
//            if (postServices.loadPostById(postid).getUser().getUserId() == userid) {
//                postData.setPostId(postid);
//                String arr[] = postServices.loadPostById(postid).getImageURL().trim().replaceAll("\\s+", " ").split(" ");
//                ArrayList<String> imagesArraylist = new ArrayList<String>(Arrays.asList(arr));
//                String newImageList="";
////                if(imageRemove!= null){
////                    for(int index:imageRemove){
////                        if(index>=arr.length)
////                            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
////                                    new ResponseObject("failed", "We have "+arr.length+" Images, can't find the remove index", ""));
////                    }
////
////                    for(int i=0; i<imageRemove.length;i++){
////                        imagesArraylist.set(imageRemove[i],"");
////                    }
////
////                    for(int i=0; i<imagesArraylist.size();i++){
////                        newImageList= newImageList + imagesArraylist.get(i)+" ";
////                    }
////                    postData.setImageURL(newImageList.replaceAll("\\s+", " ").trim());
////                    check=true;
////                }
////
////                if (file != null) {
////                    String  imagePathUploadEdit="";
////
////                    if(check) newImageList=postData.getImageURL()+" ";
////                    else  newImageList=postServices.loadPostById(postid).getImageURL().trim();
////
////                    for(int i=0; i<file.length;i++) {
////                        String fileName = imageStorageService.storeFile(file[i]);
////                        imagePathUploadEdit= imagePathUploadEdit + fileName+" ";
////
////                    }
////                    postData.setImageURL((newImageList+" "+imagePathUploadEdit).replaceAll("\\s+", " ").trim());
////                }
//
//
//                Post result = postServices.editPostDB(postData);
//                return ResponseEntity.status(HttpStatus.OK).body(
//                        new ResponseObject("ok", "Post Edit successfully", result));
//            }
//        }catch (RuntimeException exception){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    new ResponseObject("failed", "There are problem..", ""));
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                new ResponseObject("Failed", "Post Not Found", ""));
        return  null;
    }

    //______________________________________Get_post____________________________________________________//

    //    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
//    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @GetMapping("/getPosts")
    public ArrayList<com.social.app.dto.PostDTO> retrieveAllPost(){
        ArrayList<Post> result = postServices.retrivePostFromDB();
        return postServices.ArrayListPostDTO(result);
    }
    //______________________________________Get GROUP POSTS____________________________________________________//
    @GetMapping("/getPosts/{groupId}")
    public ArrayList<com.social.app.dto.PostDTO> retrievePostsFromGroup(@PathVariable("groupId")long grId){
        ArrayList<Post> result = postServices.retriveGroupPostFromDB(grId);
        return postServices.ArrayListPostDTO(result);
    }
    //______________________________________Get a Single_post____________________________________________________//
    @GetMapping("/getPost/{postId}")
    public ResponseEntity<ResponseObject> getPostById(@PathVariable("postId")long postId){
        Post post = postServices.loadPostById(postId);
        if (post == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Post ID - Not exist","Failed",""));
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Return Post","OK",postServices.MapPostDTO(post))
        );
    }
    //______________________________________Delete_post____________________________________________________//
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/deletepost/{postId}")
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
    public  ResponseEntity<ResponseObject> dislikePost(@PathVariable("postId")long postId){
        // Get user by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User current = userService.findUserByUsername(authentication.getName());
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

        // check if user already dislike, delete postlike and return
        if(likeService.postIsDisliked(postId,userId)){
            likeService.deletePostLike(postId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Like post successfully",""));
        }

        // check if user already like, delete postlike
        if(likeService.postIsLiked(postId,userId)){
            likeService.deletePostLike(postId,userId);
        }

        // else create postlike
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Dislike post successfully",likeService.createPostLike(post, current, (byte)-1))
        );
    }

    @PostMapping("/like/{postId}")
    public  ResponseEntity<ResponseObject> likePost(@PathVariable("postId")long postId){
        // Get user by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User current = userService.findUserByUsername(authentication.getName());

        // check if post is not found, return
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

        // check if user already like, delete postlike and return
        if(likeService.postIsLiked(postId,userId)){
            likeService.deletePostLike(postId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Like post successfully",""));
        }

        // check if user already dislike, delete postlike
        if(likeService.postIsDisliked(postId,userId)){
            likeService.deletePostLike(postId,userId);
        }

        // create postlike
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Like post successfully",likeService.createPostLike(post, current, (byte)1))
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
    public ArrayList<PostReportTypeDTO> getAllReportTypes(){
        return reportService.getAllPostReportTypes();
    }

    @PostMapping("/find-post")
    public ArrayList<PostResponse> findPost(@RequestBody String findContent){
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
    @GetMapping("/groupPost/{groupId}")
    public ArrayList<PostResponse> groupPost(@PathVariable Long groupId){
        ArrayList<Post> allPostGroup = postServices.retriveGroupPostFromDB(groupId);

        return responseConvertService.postResponseArrayList(allPostGroup);
    }

    @PostMapping("/donate/{postid}")
    public ResponseEntity<ResponseObject> donate(@PathVariable long postid,@RequestParam("coins")int coins){
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
    // Lay 30 bai viet cho home page
    @GetMapping("/getPostDTO-homepage")
    public ArrayList<com.social.app.dto.PostDTO> getRandomPostHomePage(){
        ArrayList<Post> allPosts = postServices.retrivePostFromDB();
       // Sap xep theo thoi gian uu tien 3
        Collections.sort(allPosts, new Comparator<Post>() {
            public int compare(Post x, Post y) {
                return y.getTime().compareTo(x.getTime());
            }
        });
        ArrayList<Post> returnPosts = new ArrayList<>();
        for(int i = 0; i<30; i++){
            if(i==allPosts.size()) break;
            returnPosts.add(allPosts.get(i));
        }

        // Sap xep theo tong tuong tac uu tien 2
        Collections.sort(returnPosts, new Comparator<Post>() {
            public int compare(Post x, Post y) {
                return (y.cmtNumbers()+y.likeNumbers()) - (x.cmtNumbers()+x.likeNumbers());
            }
        });
        // Sap xep theo tong likes uu tien nhat
        Collections.sort(returnPosts, new Comparator<Post>() {
            public int compare(Post x, Post y) {
                return y.likeNumbers() - x.likeNumbers();
            }
        });

        return postServices.ArrayListPostDTO(returnPosts);
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

    @GetMapping("/getPostDTObygroup/{groupid}")
    public ResponseEntity<ResponseObject> getPostByGroup (@PathVariable long groupid) {
        if (groupServices.loadGroupById(groupid) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "Group not exist", "")
            );
        else {
            ArrayList<Post> result = postServices.retriveGroupPostFromDB(groupid);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Success", "Done!", postServices.ArrayListPostDTO(result))
            );
        }
    }

    @GetMapping("/search")
//    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    public ArrayList<com.social.app.dto.PostDTO> search(@RequestParam("key") String keyword) {
        return postServices.ArrayListPostDTO(postServices.fullTextSearch(keyword));
    }

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @GetMapping("/getPostByUserLike")
    public ResponseEntity<ResponseObject> getPostByLike(){
        // Get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        // Get post by user like
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Success", "Found", likeService.getPostByUserLike(user.getUserId()))
        );
    }

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @GetMapping("/getPostByUserComment")
    public ResponseEntity<ResponseObject> getPostByComment(){
        // Get user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        // Get post by user comment
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Success", "Found", commentService.getPostByUserComment(user.getUserId()))
        );
    }

    @GetMapping("/count-post-report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> countPostReport() {
        long result = reportService.countPostReports();
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Empty", "No data", ""));
        } else return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Count Success", result));
    }

    @GetMapping("/count-comment-report")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> countCommentReport() {
        long result = reportService.countCommentReports();
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Empty", "No data", ""));
        } else return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Count Success", result));
    }

    @GetMapping("/count-all-report")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> countAllReport() {
        long cmt = reportService.countCommentReports();
        long post =  reportService.countPostReports();
        long result = cmt+post;
        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Empty", "No data", ""));
        } else return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Count Success", result));
    }


}