package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postId;

    private String content;
    private Timestamp time;
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

    @JsonManagedReference(value = "post_comment")
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<PostReport> reports;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes;





}