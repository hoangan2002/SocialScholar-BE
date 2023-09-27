package com.social.app.repository;

import com.social.app.model.Comment;
import com.social.app.model.CommentLike;
import com.social.app.model.Post;
import com.social.app.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    public ArrayList<CommentLike> findByComment(Comment comment);
}
