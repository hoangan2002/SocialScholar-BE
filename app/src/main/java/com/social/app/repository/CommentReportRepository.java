package com.social.app.repository;

import com.social.app.model.Comment;
import com.social.app.model.CommentReport;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    public ArrayList<CommentReport> findByComment(Comment comment);
    public void deleteByUser(User user);
}
