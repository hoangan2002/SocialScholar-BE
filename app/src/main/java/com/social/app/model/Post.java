package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;

    @Column(columnDefinition="NTEXT")
    private String content;
    private Date time;

    @Column(columnDefinition="NTEXT")
    private String imageURL;

    //quy tắc đặt tên cho JSReference "classBack_classRef"
    @JsonBackReference(value = "post_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;


    @JsonBackReference(value = "post_groups")
    @ManyToOne
    @JoinColumn(name="group_Id")
    private Groups group;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<PostReport> reports;
//
    @OneToMany(mappedBy = "post")
    private List<PostLike> likes;





}