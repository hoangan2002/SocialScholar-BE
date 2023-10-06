package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

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

    @JsonManagedReference(value = "join_user")
    @OneToMany(mappedBy = "user")
    List<JoinManagement> joins;

    @OneToMany(mappedBy = "author")
    List<Document> documents;

    @OneToMany(mappedBy = "user")
    List<Bill> bills;

    @JsonManagedReference(value = "exchange_user")
    @OneToMany(mappedBy = "user")
    List<ExchangeRequest> exchangeRequests;

    @OneToMany(mappedBy = "user")
    List<Rating> ratings;

    @JsonManagedReference(value = "commentLike_user")
    @OneToMany(mappedBy = "user")
    List<CommentLike> commentLikes;

    @JsonManagedReference(value = "postLike_user")
    @OneToMany(mappedBy = "user")
    List<PostLike> postLikes;

    @JsonManagedReference(value = "commentReport_user")
    @OneToMany(mappedBy = "user")
    List<CommentReport> commentReports;

    @JsonManagedReference(value = "postReport_user")
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
