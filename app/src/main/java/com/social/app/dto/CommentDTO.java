package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.*;
import com.social.app.repository.CommentRepository;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private long commentId;
    private String content;
    private Timestamp time;
    private long commentParentId;
    private Post post;
    private User user;
    private String author;
    private ArrayList<CommentDTO> commentChildren;
    private List<CommentLikeDTO> likes;
    @JsonView(Views.CommentView.class)
    public long getCommentId() {
        return commentId;
    }
    @JsonView(Views.CommentView.class)
    public String getContent() {
        return content;
    }
    @JsonView(Views.CommentView.class)
    public Timestamp getTime() {
        return time;
    }
    @JsonView(Views.CommentView.class)
    public long getCommentParentId() {
        return commentParentId;
    }
    @JsonView(Views.CommentView.class)
    public long getPost() {
        return post.getPostId();
    }
    @JsonView(Views.CommentView.class)
    public int getUser() {
        return user.getUserId();
    }
    @JsonView(Views.CommentView.class)
    public String getAuthor() {
        return user.getUserName();
    }
    @JsonView(Views.CommentView.class)
    public ArrayList<CommentDTO> getCommentChildren() {
        return this.commentChildren;
    }
    @JsonView(Views.CommentView.class)
    public List<CommentLikeDTO> getLikes() {
        return likes;
    }

    public void setCommentChildren(ArrayList<CommentDTO> commentChildren){
        this.commentChildren = commentChildren;
    }


}



