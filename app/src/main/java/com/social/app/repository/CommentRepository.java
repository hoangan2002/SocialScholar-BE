package com.social.app.repository;

import com.social.app.model.Comment;
import com.social.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public ArrayList<Comment> findByPost(Post post);
    public Comment findByCommentId(long commentId);

    public ArrayList<Comment> findByCommentParentId(long commentParentId);
}
