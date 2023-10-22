package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Post_Report")
public class PostReport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long reportId;
    private String description;
    private Date time;
    private boolean isChecked;
    @JsonBackReference(value = "postReport_type")
    @ManyToOne
    @JoinColumn(name="type_Id")
    private PostReportType postReportType;

    @JsonBackReference(value = "postReport_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @JsonBackReference(value = "post_report")
    @ManyToOne
    @JoinColumn(name="post_Id")
    private Post post;
}
