package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.model.Comment;
import com.social.app.model.Post;
import com.social.app.repository.PostRepository;
import com.social.app.service.CommentService;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/api/comment-services")
public class CommentController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private PostRepository postRepository;
    @PostMapping("/{postID}/comments")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<ResponseObject> createComment(@RequestPart Comment comment,
                                                 @RequestParam("userid") int userid,
                                                 @PathVariable long postID){
        try {
            // set user for comment
            comment.setUser(userService.loadUserById(userid));
            // create comment
            Comment newComment = this.commentService.createComment(comment, postID);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Success", "Create new comment successfully", newComment));
        }
        catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "There are problem..", ""));
        }
    }

    @DeleteMapping("/delete/{commentID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<ResponseObject> deleteComment(@PathVariable long commentID){
        try {
            this.commentService.deleteComment(commentID);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "Delete comment successfully", ""));
        }
        catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "There are problem..", ""));
        }
    }

    @GetMapping("/{postID}/all-comments")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ArrayList<Comment> getAllComments(@PathVariable long postID){
        ArrayList<Comment> allComments = this.commentService.getAllComments(postID);
        return allComments;
    }

    @PutMapping("/edit/{commentID}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    ResponseEntity<ResponseObject> editComment(@PathVariable long commentID, @RequestBody Comment newComment){
        try {
            Comment editedComment = this.commentService.editComment(newComment, commentID);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Success", "Edit comment successfully", editedComment));
        }
        catch (RuntimeException runtimeException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "There are problem..", ""));
        }
    }




}
