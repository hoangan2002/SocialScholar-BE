package com.social.app.repository;

import com.social.app.model.Post;
import com.social.app.model.PostLike;
import com.social.app.model.User;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    public ArrayList<PostLike> findByPost(Post post);
    public PostLike findByLikeId(long id);

    public ArrayList<PostLike> findByUser(User user);
}
