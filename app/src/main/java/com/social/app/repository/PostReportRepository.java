package com.social.app.repository;

import com.social.app.model.Post;
import com.social.app.model.PostReport;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    public ArrayList<PostReport> findByPost(Post post);
    ArrayList<PostReport> findAll();
    public void deleteByUser(User user);
}
