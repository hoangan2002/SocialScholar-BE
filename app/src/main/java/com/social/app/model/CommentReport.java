package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.springframework.data.web.JsonPath;

import java.util.Date;

@Entity
@Table(name = "Comment_Report")
public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reportId;
    private String type;
    private String description;
    private Date time;

    @JsonBackReference(value = "commentReport_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @JsonBackReference(value = "comment_report")
    @ManyToOne
    @JoinColumn(name="commentId")
    private Comment comment;

}
