package com.social.app.controller;

import com.social.app.dto.*;
import com.social.app.entity.ResponseObject;
import com.social.app.model.*;
import com.social.app.request.CommentRequest;
import com.social.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;

@RestController
@RequestMapping(path="/api/comment-services")
public class CommentController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private PostServices postServices;
    @Autowired
    private ReportService reportService;



    @PostMapping("/{postID}/comments")
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    ResponseEntity<ResponseObject> createNewComment(@RequestBody CommentDTO commentDTO,
                                                 @PathVariable long postID){
        // Get userId by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = userService.findUserByUsername(authentication.getName()).getUserName();
        try {

            // Get post from postId
            Post post = postServices.loadPostById(postID);
            // Check if user is not in group, user can not create comment

            if(!userService.isGroupMember(username, post.getGroup().getGroupId())) throw new RuntimeException("Must be group member");

            if(!userService.isGroupMember( post.getGroup().getGroupId())) throw new RuntimeException("Must be group member");
            // create comment
            CommentDTO newComment = this.commentService.createComment(commentDTO, postID, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ResponseObject("Successfully", "Create comment successfully", newComment));
        }
        catch (RuntimeException runtimeException){
            runtimeException.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "There are problem..", ""));
        }
    }

    @PostMapping("/delete/{commentID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<ResponseObject> deleteComment(@PathVariable long commentID){
        // Get userId by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userService.findUserByUsername(authentication.getName()).getUserId();

        try {
            // Get comment from commentId
            CommentDTO comment = commentService.getCommentByID(commentID);
            // Check if user don't create comment, user can not delete
            if(!userService.isCommemtCreator(userId, commentID))  throw new RuntimeException("Must be group member");
            // Call delete method
            this.commentService.deleteComment(commentID);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "Delete comment successfully", ""));
        }
        catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "There are problem..", ""));
        }
    }

    @PostMapping("/all-comments/{postID}")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    ArrayList<CommentDTO> getAllComments(@PathVariable long postID){
        ArrayList<CommentDTO> allComments = this.commentService.getAllComments(postID);
        return allComments;
    }

    @PutMapping("/edit/{commentID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<ResponseObject> editComment(@PathVariable long commentID, @RequestPart Comment newComment){
        // Get userId by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userService.findUserByUsername(authentication.getName()).getUserId();

        try {
            // Get comment from commentId
            CommentDTO comment = commentService.getCommentByID(commentID);
            // Check if user don't create comment, user can not edit
            if(!userService.isCommemtCreator(userId, commentID)) throw new RuntimeException("Must be group member");
            // Call edit method
            CommentDTO editedComment = this.commentService.editComment(newComment, commentID);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "Edit comment successfully", editedComment));
        }
        catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "There are problem..", ""));
        }
    }


    @PostMapping("/dislike/{commentId}")
    public  ResponseEntity<ResponseObject> dislikeComment(@PathVariable long commentId){
        // Get user by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        int userId = user.getUserId();

        // check if comment is not found, return
        if (commentService.getCommentByID(commentId)== null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed","Can't find comment","")
            );

        CommentDTO comment = commentService.getCommentByID(commentId);
        // Map commentDTO to comment


        // Check if user is not in group, user can not dislike comment
        Post post = commentService.getPostByCommentId(commentId);
        if(!userService.isGroupMember(post.getGroup().getGroupId())) throw new RuntimeException("Must be group member");

        // check if user already dislike, delete commentlike and return
        if(likeService.commentIsDisliked(commentId,userId)){
            likeService.deleteCommentLike(commentId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Dislike comment successfully",""));
        }

        // check if user already like, delete commentlike
        if(likeService.commentIsLiked(commentId,userId)){
            likeService.deleteCommentLike(commentId,userId);
        }

        // else create commentlike
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Dislike comment successfully",likeService.createCommentLike(comment, user, (byte)-1))
        );
    }

    @PostMapping("/like/{commentId}")
    public  ResponseEntity<ResponseObject> likePost(@PathVariable long commentId){
        // Get user by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authentication.getName());
        int userId = user.getUserId();

        // check if comment is not found, return
        if (commentService.getCommentByID(commentId)== null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed","Can't find comment","")
            );

        CommentDTO comment = commentService.getCommentByID(commentId);
        // Check if user is not in group, user can not like comment
        Post post = commentService.getPostByCommentId(commentId);
        if(!userService.isGroupMember(post.getGroup().getGroupId())) throw new RuntimeException("Must be group member");

        // check if user already like, delete commentlike and return
        if(likeService.commentIsLiked(commentId,userId)){
            likeService.deleteCommentLike(commentId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Like comment successfully",""));
        }

        // check if user already dislike, delete commentlike
        if(likeService.commentIsDisliked(commentId,userId)){
            likeService.deleteCommentLike(commentId,userId);
        }

        // else create commentlike
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Like comment successfully",likeService.createCommentLike(comment, user, (byte)1))
        );
    }

    @GetMapping("/getLike/{commentId}")
    public int getCommentLike(@PathVariable long commentId){
        return likeService.getTotalCommentLike(commentId);
    }

    @PostMapping("/reply-comments/{commentParentId}")
    ResponseEntity<ResponseObject> replyComment(@RequestPart Comment commentReply,
                                                 @RequestParam("userid") int userid,
                                                 @PathVariable long commentParentId){
        // Get userId by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userService.findUserByUsername(authentication.getName()).getUserId();

        try {
            // Get commentParent from commentParentId
            CommentDTO commentParent = commentService.getCommentByID(commentParentId);
            // Check if user is not in group, user can not create comment
            Post post = commentService.getPostByCommentId(commentParentId);
            if(!userService.isGroupMember( post.getGroup().getGroupId())) throw new RuntimeException("Must be group member");
            // set user for comment
            commentReply.setUser(userService.loadUserById(userid));
            // create comment
            CommentDTO newComment = this.commentService.createCommentReply(commentReply, commentParentId);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Success", "Create new comment successfully", newComment));
        }
        catch (RuntimeException runtimeException){
            runtimeException.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "There are problem..", ""));
        }
    }

    @PostMapping("/report/{commentId}")
    public  ResponseEntity<ResponseObject> reportPost(@PathVariable long commentId, @RequestParam("typeid") int typeId,
                                                      @RequestParam("description") String description){
        // Get userId by token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = userService.findUserByUsername(authentication.getName()).getUserId();

        // check if comment is not found, return
        if (commentService.getCommentByID(commentId)== null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed","Can't find comment","")
            );

        CommentDTO comment = commentService.getCommentByID(commentId);
        // Check if user is not in group, user can not report comment
        Post post = commentService.getPostByCommentId(commentId);
        if(!userService.isGroupMember( post.getGroup().getGroupId()))
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed","User must be in group","")
            );

        // set description to report
        CommentReport commentReport = new CommentReport();
        commentReport.setDescription(description);

        // Create user
        User user = userService.loadUserById(userId);

        // create postreport
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Report comment successfully",reportService.createCommentReport (comment, user, commentReport, typeId))
        );
    }

    @GetMapping("/all-reports/{commentId}")
    public ArrayList<CommentReportDTO> getAllReportsByCommentId(@PathVariable long commentId){
        return reportService.getAllCommentReportsByCommentId(commentId);
    }

    @GetMapping("/all-reports")
    public  ArrayList<CommentReportDTO> getAllReports(){
        return reportService.getAllCommentReports();
    }

    @GetMapping("/all-report-types")
    public ArrayList<CommentReportTypeDTO> getAllReportTypes(){
        return reportService.getAllCommentReportTypes();
    }
}
