package com.social.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Comment_Like")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long likeId;

    private byte status;

    private Date time;

    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @ManyToOne
    @JoinColumn(name="comment_Id")
    private Comment commentLike;

}
