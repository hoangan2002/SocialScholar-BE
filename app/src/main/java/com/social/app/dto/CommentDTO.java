package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.social.app.model.CommentLike;
import com.social.app.model.CommentReport;
import com.social.app.model.Post;
import com.social.app.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
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
    private String post;
    private User user;
    private List<CommentLike> likes;
}
