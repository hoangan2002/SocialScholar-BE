package com.social.app.model;

import jakarta.persistence.*;
import org.springframework.data.web.JsonPath;

import java.util.Date;

public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reportId;
    private String type;
    private String description;
    private Date time;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @JoinColumn(name="commentId")
    private Comment comment;

}
