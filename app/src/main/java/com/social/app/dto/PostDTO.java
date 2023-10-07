package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
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
    private String imageURL;
    private String titles;
    private User user;
    private Groups group;
    private List<PostLike> likes;
    private List<Comment> comments;

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

    @JsonView(Views.CommentView.class)
    public int getUser() {
        return user.getUserId();
    }

    @JsonView(Views.PostView.class)
    public long getGroup() {
        return group.getGroupId();
    }

    @JsonView(Views.PostView.class)
    public int getLikes() {
        return likes.size();
    }

    @JsonView(Views.PostView.class)
    public int getComments() {return comments.size();}


}
