package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    private String userName;
    private String phone;
    private String password;
    private String email;
    private String avatarURL;
    private String level;
    private String role;
    private long coin;
    private int activityPoint;
    private boolean isLocked;

    @OneToMany(mappedBy = "user")
    List<JoinManagement> joins;

    @OneToMany(mappedBy = "author")
    List<Document> documents;

    @OneToMany(mappedBy = "user")
    List<Bill> bills;

    @OneToMany(mappedBy = "user")
    List<Rating> ratings;

    @OneToMany(mappedBy = "user")
    List<CommentLike> commentLikes;

    @JsonManagedReference(value = "like_user")
    @OneToMany(mappedBy = "user")
    List<PostLike> postLikes;

    @OneToMany(mappedBy = "user")
    List<CommentReport> commentReports;

    @OneToMany(mappedBy = "user")
    List<PostReport> postReports;

    @JsonManagedReference(value = "comment_user")
    @OneToMany(mappedBy = "user")
    List<Comment> comments;

    @JsonManagedReference(value = "post_user")
    @OneToMany(mappedBy = "user")
    List<Post> posts;

    @OneToMany(mappedBy = "hosts")
    List<Groups> groups;

    @OneToMany(mappedBy = "user")
    List<TokenPaymentHistory> tokenPaymentHistories;
//
    @OneToMany(mappedBy = "user")
    List<LoginHistory> loginHistories;


}
