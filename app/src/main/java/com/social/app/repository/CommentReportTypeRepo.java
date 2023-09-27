package com.social.app.repository;

import com.social.app.model.CommentReport;
import com.social.app.model.CommentReportType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportTypeRepo extends JpaRepository<CommentReportType, Integer> {

}
