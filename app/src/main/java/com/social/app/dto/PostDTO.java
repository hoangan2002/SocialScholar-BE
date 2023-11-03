package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.entity.CommentResponse;
import com.social.app.entity.LikeResponse;
import com.social.app.model.*;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private long postId;
    private String content;
    private Timestamp time;
    private String titles;
    private String imageURL;
    private User user;
    private String author;
    private int authorPoints;
    private long groupId;
    private Groups group;
    private List<CommentDTO> comments;
    private List<PostLikeDTO> likes;
    private List<PostReportDTO> reports;
    private List<PostReportDTO> report;
    @JsonView(Views.PostView.class)
    public int getAuthorPoints() {
        return user.getActivityPoint();
    }

    @JsonView(Views.PostView.class)
    public long getPostId() {
        return postId;
    }

    @JsonView(Views.PostView.class)
    public long getGroupId() {
        return group.getGroupId();
    }


    @JsonView(Views.PostView.class)
    public String getContent() {
        return content;
    }

    @JsonView(Views.PostView.class)
    public String getAuthor() {
        return user.getUserName();
    }

    @JsonView(Views.PostView.class)
    public Timestamp getTime() {
        return time;
    }

    @JsonView(Views.PostView.class)
    public Groups group() {
        return null;
    }

    @JsonView(Views.PostView.class)
    public String getTitles() {
        return titles;
    }

    @JsonView(Views.PostView.class)
    public String getGroupName() {
        return group.getGroupName();}

    @JsonView(Views.PostView.class)
    public String getImageURL() {
        return imageURL;
    }
    @JsonView(Views.PostView.class)
    public String getUser() {return user.getUserName();}
    @JsonView(Views.PostView.class)
    public List<PostLikeDTO> getLikes() {return likes;}
    @JsonView(Views.PostView.class)
    public List<PostReportDTO> getReports() {return reports;}
    @JsonView(Views.PostView.class)
    public List<PostReportDTO> getReport() {return reports;}
    @JsonView(Views.PostView.class)
    public List<CommentDTO> getComments() {return comments;}


}