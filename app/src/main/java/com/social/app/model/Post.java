package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_Id")
    private User user;

    @JsonBackReference(value = "post_groups")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="group_Id")
    private Groups group;

    @OneToMany(mappedBy = "post")
    @JsonManagedReference(value = "post_comment")
    private List<Comment> comments;

    @JsonManagedReference(value = "post_report")
    @OneToMany(mappedBy = "post")
    private List<PostReport> reports;


    @OneToMany(mappedBy = "post")
    @JsonManagedReference(value = "post_like")
    private List<PostLike> likes;

    public int countLike(){
        int like = 0;
        for(PostLike pl : this.getLikes()){
            if(pl.getStatus()==1) like=like+1;
            else like=like-1;
        }
        return like;
    }

}