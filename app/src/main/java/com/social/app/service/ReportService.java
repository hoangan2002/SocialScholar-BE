package com.social.app.service;

import com.social.app.dto.CommentDTO;
import com.social.app.dto.CommentReportDTO;
import com.social.app.dto.PostReportDTO;
import com.social.app.model.*;
import com.social.app.repository.CommentReportRepository;
import com.social.app.repository.CommentReportTypeRepo;
import com.social.app.repository.PostReportRepository;
import com.social.app.repository.PostReportTypeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    ModelMapper modelMapper;

    public ArrayList<PostReport> getAllPostReportsByPostId(long postId){
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

    public ArrayList<CommentReportDTO> getAllCommentReportsByCommentId(long commentId){
        CommentDTO commentDTO = commentService.getCommentByID(commentId);
        // Map commentDTO to comment;
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        // Find comment report
        List<CommentReport> commentReports = commentReportRepository.findByComment(comment);
        ArrayList<CommentReportDTO> commentReportDTOs = new ArrayList<>();
        // Map commentReport to commentReportDTO then return
        for (CommentReport commentReport: commentReports) {
            commentReportDTOs.add(modelMapper.map(commentReport, CommentReportDTO.class));
        }
        return commentReportDTOs;
    }

    public CommentReport createCommentReport(CommentDTO commentDTO, User user, CommentReport commentReport, int typeId){
        // Map commentDTO to comment;
        Comment comment = modelMapper.map(commentDTO, Comment.class);
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

    public ArrayList<CommentReportDTO> getAllCommentReports(){
        List<CommentReport> commentReports = commentReportRepository.findAll();
        ArrayList<CommentReportDTO> commentReportDTOs = new ArrayList<>();
        for (CommentReport commentReport: commentReports) {
            commentReportDTOs.add(modelMapper.map(commentReport, CommentReportDTO.class));
        }
        return commentReportDTOs;
    }

    public ArrayList<PostReportDTO> getAllPostReports(){
        List<PostReport> postReports = postReportRepository.findAll();
        ArrayList<PostReportDTO> postReportDTOs = new ArrayList<>();
        for (PostReport postReport: postReports) {
            postReportDTOs.add(modelMapper.map(postReport, PostReportDTO.class));
        }
        return postReportDTOs;
    }
}
