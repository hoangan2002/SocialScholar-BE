package com.social.app.service;

import com.social.app.model.CommentLike;
import com.social.app.model.Post;
import com.social.app.model.PostLike;
import com.social.app.model.User;
import com.social.app.repository.CommentLikeRepository;
import com.social.app.repository.PostLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class LikeService {
    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private CommentLikeRepository commentLikeRepository;

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

    public CommentLike createCommentLike(long commentId, int userId, byte status){

        return null;
    }

    public void deletePostLike(){

    }

    public void deleteCommentLike(){

    }


}
