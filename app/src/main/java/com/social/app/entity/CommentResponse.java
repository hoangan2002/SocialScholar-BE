package com.social.app.entity;

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
public class CommentResponse {
    private long commentId;
    private long postId;
    private long commentParentId;
    private String content;
    private Timestamp time;
    private String author;
    private List<CommentReport> reports;
    private List<LikeResponse> likes;
}
