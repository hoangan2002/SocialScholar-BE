package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Getter
@Setter
public class PostSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long saveId;
    private Timestamp time;
    @JsonBackReference(value = "postSave_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @JsonBackReference(value = "postSave_post")
    @ManyToOne
    @JoinColumn(name="post_Id")
    private Post post;
}
