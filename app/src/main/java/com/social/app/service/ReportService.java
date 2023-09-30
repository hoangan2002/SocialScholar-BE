package com.social.app.service;

import com.social.app.model.*;
import com.social.app.repository.CommentReportRepository;
import com.social.app.repository.CommentReportTypeRepo;
import com.social.app.repository.PostReportRepository;
import com.social.app.repository.PostReportTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ReportService {

    @Autowired
    PostReportRepository postReportRepository;
    @Autowired
    CommentReportRepository commentReportRepository;
    @Autowired
    PostReportTypeRepo postReportTypeRepo;
    @Autowired
    CommentReportTypeRepo commentReportTypeRepo;
    @Autowired
    PostServices postServices;
    @Autowired
    CommentService commentService;
    public ArrayList<PostReport> getAllPostReports(long postId){
        Post post = postServices.loadPostById(postId);
        return postReportRepository.findByPost(post);
    }

    public PostReport createPostReport(Post post, User user, PostReport postReport, int typeId){
        // set info to report
        postReport.setPost(post);
        postReport.setUser(user);
        postReport.setPostReportType(postReportTypeRepo.findById(typeId).orElseThrow(() -> new RuntimeException("Can't find type")));

        // set current time to report
        Date date = new Date();
        Timestamp datetime = new Timestamp(date.getTime());
        postReport.setTime(datetime);

        return postReportRepository.save(postReport);
    }

    public ArrayList<CommentReport> getAllCommentReports(long commentId){
        Comment comment = commentService.getCommentByID(commentId);
        return commentReportRepository.findByComment(comment);
    }

    public CommentReport createCommentReport(Comment comment, User user, CommentReport commentReport, int typeId){
        // set info to report
        commentReport.setComment(comment);
        commentReport.setUser(user);
        commentReport.setCommentReportType(commentReportTypeRepo.findById(typeId).orElseThrow(() -> new RuntimeException("Can't find type")));

        // set current time to report
        Date date = new Date();
        Timestamp datetime = new Timestamp(date.getTime());
        commentReport.setTime(datetime);

        return commentReportRepository.save(commentReport);
    }

    public ArrayList<PostReportType> getAllPostReportTypes(){
        return new ArrayList<>(postReportTypeRepo.findAll());
    }

    public ArrayList<CommentReportType> getAllCommentReportTypes(){
        return new ArrayList<>(commentReportTypeRepo.findAll());
    }
}
