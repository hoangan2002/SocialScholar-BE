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
    private Groups group;
    private List<CommentDTO> comments;
    private List<PostLikeDTO> likes;

    @JsonView(Views.PostView.class)
    public long getPostId() {
        return postId;
    }

    @JsonView(Views.PostView.class)
    public String getContent() {
        return content;
    }

    @JsonView(Views.PostView.class)
    public Timestamp getTime() {
        return time;
    }

    @JsonView(Views.PostView.class)
    public String getTitles() {
        return titles;
    }

    @JsonView(Views.PostView.class)
    public String getGroup() {
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
    public List<CommentDTO> getComments() {return comments;}


}
