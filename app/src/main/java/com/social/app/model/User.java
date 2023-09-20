package com.social.app.model;

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
    private String role;
    private long token;

    @OneToMany(mappedBy = "user")
    List<JoinManagement> joins;
    @OneToMany(mappedBy = "user")
    List<Document> documents;
    @OneToMany(mappedBy = "user")
    List<Bill> bills;
    @OneToMany(mappedBy = "user")
    List<Rating> ratings;
    @OneToMany(mappedBy = "user")
    List<CommentLike> commentLikes;
    @OneToMany(mappedBy = "user")
    List<PostLike> postLikes;
    @OneToMany(mappedBy = "user")
    List<CommentReport> commentReports;
    @OneToMany(mappedBy = "user")
    List<PostReport> postReports;
    @OneToMany(mappedBy = "user")
    List<Comment> comments;
    @OneToMany(mappedBy = "user")
    List<Post> posts;
    @OneToMany(mappedBy = "user")
    List<Group> myGroups;
    @OneToMany(mappedBy = "user")
    List<TokenPaymentHistory> tokenPaymentHistories;
    @OneToMany(mappedBy = "user")
    List<LoginHistory> loginHistories;
}