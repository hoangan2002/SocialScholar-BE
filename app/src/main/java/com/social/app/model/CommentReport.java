package com.social.app.model;

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

    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name="commentId")
    private Comment commentReport;

}
