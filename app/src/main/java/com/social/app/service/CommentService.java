package com.social.app.service;

import com.social.app.dto.CommentDTO;
import com.social.app.model.Comment;
import com.social.app.model.Post;
import com.social.app.repository.CommentRepository;
import com.social.app.repository.PostRepository;
import com.social.app.request.CommentRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserService userService;
    public CommentDTO createComment(CommentDTO commentDTO, long postID, int userId){
        Post post = this.postRepository.findById(postID).orElseThrow(()-> new RuntimeException());
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        // set post to comment
        comment.setPost(post);
        // set user for comment
        comment.setUser(userService.loadUserById(userId));
        // set current time to comment
        Date date = new Date();
        Timestamp datetime = new Timestamp(date.getTime());
        comment.setTime(datetime);
        return modelMapper.map(this.commentRepository.save(comment), CommentDTO.class) ;
    }
    public void deleteComment(long commentID){
        Comment com = this.commentRepository.findById(commentID).orElseThrow(()->new RuntimeException("Post not exits !"));
        this.commentRepository.delete(com);
    }

    public ArrayList<CommentDTO> getAllComments(long postID){
        // Get post by postid
        Post post = this.postRepository.findById(postID).orElseThrow(()-> new RuntimeException("Post not exits !"));
        // Get all comments children by post
        ArrayList<Comment> comments = this.commentRepository.findByPost(post);
        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment: comments) {
            // if comment has comment parent -> skip
            if (comment.getCommentParentId() != 0) continue;
            // else call setCommentDTOChildren(comment)
            CommentDTO commentDTO = this.modelMapper.map(comment, CommentDTO.class);
            setCommentDTOChildren(commentDTO);
            // add to result list
            commentDTOS.add(commentDTO);
        }
        return commentDTOS;
    }

    // Function set comment dto children for comment dto parent
    public void setCommentDTOChildren(CommentDTO commentDTO){
        // If commentDTO is not a parent -> return
        if (getAllCommentChildren(commentDTO.getCommentId()).isEmpty()) return;
        // Call getAllCommentChildren(comment parent id)
        ArrayList<CommentDTO> commentDTOChildren = getAllCommentChildren(commentDTO.getCommentId());
        // Set comment children for comment parent
        commentDTO.setCommentChildren(commentDTOChildren);
        // If comment child have their own children, set children too
        for (CommentDTO commentDTOChild: commentDTOChildren) {
            setCommentDTOChildren(commentDTOChild);
        }
    }

    public CommentDTO getCommentByID(long commentID){
        Comment comment = this.commentRepository.findById(commentID).orElseThrow(()-> new RuntimeException("Comment not exits !"));
        // map comment to commentDTO
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        return commentDTO;
    }

    public CommentDTO editComment(Comment newComment, long commentID){
        return this.commentRepository.findById(commentID).map(
                // if find comment, change comment
                comment -> {
                    comment.setContent(newComment.getContent());
                    // save comment to database, map comment to commentDTO then return
                    return modelMapper.map(this.commentRepository.save(comment), CommentDTO.class);
                }
        ).orElseThrow(()-> new RuntimeException("Comment not exits !"));
    }

    public CommentDTO createCommentReply(Comment commentReply, long commentParentId){
        Comment commentParent = this.commentRepository.findById(commentParentId).orElseThrow(()-> new RuntimeException());
        // set info to comment
        commentReply.setPost(commentParent.getPost());
        commentReply.setCommentParentId(commentParentId);

        // set current time to comment
        Date date = new Date();
        Timestamp datetime = new Timestamp(date.getTime());
        commentReply.setTime(datetime);

        // save comment to database then map comment to commentDTO
        CommentDTO commentDTO = modelMapper.map(this.commentRepository.save(commentReply), CommentDTO.class);
        return commentDTO;
    }

    // Get all commentDTO children by comment paren id
    public ArrayList<CommentDTO> getAllCommentChildren(long commentId){
        Comment commentParent = this.commentRepository.findById(commentId).orElseThrow(()-> new RuntimeException());
        ArrayList<Comment> commentChildren = this.commentRepository.findByCommentParentId(commentId);
        ArrayList<CommentDTO> commentDTOChildren = new ArrayList<>();
        for (Comment commentChild:commentChildren) {
            commentDTOChildren.add(this.modelMapper.map(commentChild,CommentDTO.class));
        }
        return commentDTOChildren;
    }

    public Post getPostByCommentId(long commentId){
        Comment comment = commentRepository.findByCommentId(commentId);
        return comment.getPost();
    }
}
