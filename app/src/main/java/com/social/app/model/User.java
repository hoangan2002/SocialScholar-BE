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
    private String backgroundURL;
    private String level;
    private String role;
    private long coin;
    private int activityPoint;
    private boolean isLocked;

    @JsonManagedReference(value = "join_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<JoinManagement> joins;

    @JsonManagedReference(value = "document_user")
    @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
    List<Document> documents;

    @JsonManagedReference(value = "bill_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<Bill> bills;

    @JsonManagedReference(value = "rating_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<Rating> ratings;

    @JsonManagedReference(value = "commentLike_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<CommentLike> commentLikes;

    @JsonManagedReference(value = "postLike_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<PostLike> postLikes;

    @JsonManagedReference(value = "commentReport_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<CommentReport> commentReports;

    @JsonManagedReference(value = "postReport_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<PostReport> postReports;

    @JsonManagedReference(value = "comment_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<Comment> comments;

    @JsonManagedReference(value = "post_user")
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    List<Post> posts;

    @OneToMany(mappedBy = "hosts")
    @JsonManagedReference(value = "group_user")
    List<Groups> groups;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference(value = "payment_user")
    List<TokenPaymentHistory> tokenPaymentHistories;


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference(value = "postSave_user")
    List<PostSave> postSaves;

}
