package com.social.app.repository;

import com.social.app.model.Comment;
import com.social.app.model.Post;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public ArrayList<Comment> findByPostId(Post post);
    public Comment findByCommentId(long commentId);

    public ArrayList<Comment> findByCommentParentId(long commentParentId);
    public ArrayList<Comment> findByUser(User user);
    public void deleteByUser(User user);
}
