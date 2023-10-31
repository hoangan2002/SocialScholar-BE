package com.social.app.repository;

import com.social.app.model.Post;
import com.social.app.model.PostSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostSaveRepository extends JpaRepository<PostSave, Long> {
    public List<PostSave> findByPost(Post post);
}
