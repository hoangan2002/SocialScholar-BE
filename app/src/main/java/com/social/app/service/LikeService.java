package com.social.app.service;

import com.social.app.model.*;
import com.social.app.repository.CommentLikeRepository;
import com.social.app.repository.CommentRepository;
import com.social.app.repository.PostLikeRepository;
import com.social.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Service
public class LikeService {
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    public PostLike createPostLike(Post post, User user, byte status){
        PostLike postLike = new PostLike();
        // set user, post and status for postlike
        postLike.setUser(user);
        postLike.setPost(post);
        postLike.setStatus(status);

        // set current time for postlike
        Date date = new Date();
        Timestamp datetime = new Timestamp(date.getTime());
        postLike.setTime(datetime);

        // save postlike
        return postLikeRepository.save(postLike);
    }

    public CommentLike createCommentLike(Comment comment, User user, byte status){
        CommentLike commentLike = new CommentLike();
        // set user, post and status for commentlike
        commentLike.setUser(user);
        commentLike.setComment(comment);
        commentLike.setStatus(status);

        // set current time for commentlike
        Date date = new Date();
        Timestamp datetime = new Timestamp(date.getTime());
        commentLike.setTime(datetime);

        // save postlike
        return commentLikeRepository.save(commentLike);
    }


    public void deletePostLike(PostLike postLike){
        postLikeRepository.delete(postLike);
    }

    public void deleteCommentLike(CommentLike commentLike){
        commentLikeRepository.delete(commentLike);
    }

    public PostLike getPostLike(long postId, int userId){
        Post post = postRepository.findByPostId(postId);
        ArrayList<PostLike> postLikeList = postLikeRepository.findByPost(post);
        for (PostLike postLike:postLikeList) {
            if (postLike.getUser().getUserId() == userId)
                return postLike;
        }
        return null;
    }

    public CommentLike getCommentLike(long commentId, int userId){
        Comment comment = commentRepository.findByCommentId(commentId);
        ArrayList<CommentLike> commentLikes = commentLikeRepository.findByComment(comment);
        for (CommentLike commentLike:commentLikes) {
            if (commentLike.getUser().getUserId() == userId)
                return commentLike;
        }
        return null;
    }

    public int getTotalPostLike(long postId){
        Post post = postRepository.findByPostId(postId);
        ArrayList<PostLike> postLikeList = postLikeRepository.findByPost(post);
        int totalLike = 0;
        for (PostLike postLike: postLikeList) {
            totalLike += postLike.getStatus();
        }
        return totalLike;
    }

    public int getTotalCommentLike(long commentId){
        Comment comment = commentRepository.findByCommentId(commentId);
        ArrayList<CommentLike> commentLikes = commentLikeRepository.findByComment(comment);
        int totalLike = 0;
        for (CommentLike commentLike: commentLikes) {
            totalLike += commentLike.getStatus();
        }
        return totalLike;
    }
}
