package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

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

    @JsonBackReference(value = "commentLike_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

    @JsonBackReference(value = "comment_like")
    @ManyToOne
    @JoinColumn(name="comment_Id")
    private Comment comment;

}
