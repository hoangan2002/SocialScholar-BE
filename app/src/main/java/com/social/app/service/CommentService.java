package com.social.app.service;

import com.social.app.model.Comment;
import com.social.app.model.Post;
import com.social.app.repository.CommentRepository;
import com.social.app.repository.PostRepository;
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
    public Comment createComment(Comment comment, long postID){
        Post post = this.postRepository.findById(postID).orElseThrow(()-> new RuntimeException());
        // set post to comment
        comment.setPost(post);

        // set current time to comment
        Date date = new Date();
        Timestamp datetime = new Timestamp(date.getTime());
        comment.setTime(datetime);
        return this.commentRepository.save(comment);
    }
    public void deleteComment(long commentID){
        Comment com = this.commentRepository.findById(commentID).orElseThrow(()->new RuntimeException("Post not exits !"));
        this.commentRepository.delete(com);
    }

    public ArrayList<Comment> getAllComments(long postID){
        Post post = this.postRepository.findById(postID).orElseThrow(()-> new RuntimeException("Post not exits !"));
        return this.commentRepository.findByPost(post);
    }

    public Comment getCommentByID(long commentID){
        Comment comment = this.commentRepository.findById(commentID).orElseThrow(()-> new RuntimeException("Comment not exits !"));
        return comment;
    }

    public Comment editComment(Comment newComment, long commentID){
        return this.commentRepository.findById(commentID).map(
                // if find comment, change comment
                comment -> {
                    comment.setContent(newComment.getContent());
                    return this.commentRepository.save(comment);
                }
        ).orElseThrow(()-> new RuntimeException("Comment not exits !"));
    }

    public Comment createCommentReply(Comment commentReply, long commentParentId){
        Comment commentParent = this.commentRepository.findById(commentParentId).orElseThrow(()-> new RuntimeException());
        // set info to comment
        commentReply.setPost(commentParent.getPost());
        commentReply.setCommentParentId(commentParentId);

        // set current time to comment
        Date date = new Date();
        Timestamp datetime = new Timestamp(date.getTime());
        commentReply.setTime(datetime);
        return this.commentRepository.save(commentReply);
    }
}
