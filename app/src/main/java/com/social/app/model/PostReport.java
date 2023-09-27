package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Post_Report")
public class PostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reportId;
    private String type;
    private String description;
    private Date time;

    @JsonBackReference(value = "postReport_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @JsonBackReference(value = "post_report")
    @ManyToOne
    @JoinColumn(name="post_Id")
    private Post post;
}
