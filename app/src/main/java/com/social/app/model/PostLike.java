package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.GsonBuilder;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Post_Like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long likeId;

    private Date time;


    private byte status;

    @JsonBackReference(value = "like_user")
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;


    @ManyToOne
    @JsonBackReference(value = "post_like")
    @JoinColumn(name="postId")
    private Post post;

    @Override
    public String toString() {
        return "PostLike{" +
                "likeId=" + likeId +
                ", time=" + time +
                ", user=" + user +
                ", post=" + post +
                '}';
    }


}
