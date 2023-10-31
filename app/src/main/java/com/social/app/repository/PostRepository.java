package com.social.app.repository;

import com.social.app.model.Comment;
import com.social.app.model.Document;
import com.social.app.model.Post;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    ArrayList<Post> findAll();
    Post save(Post post);
    void deleteById(long id);
    Post findByPostId(long postId);
    ArrayList<Post> findAllByGroupGroupId(long groupId);
    @Query(value = "SELECT * FROM post WHERE MATCH (titles) AGAINST (?1)> 0 ORDER BY MATCH (titles) AGAINST (?1) DESC", nativeQuery = true)
    ArrayList<Post> fullTextSearch(String keyword);
}
