package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Post;
import com.social.app.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDTO {
    private long likeId;
    private byte status;
    private Date time;
    private User user;
    private Post post;

    @JsonView({Views.PostLikeView.class})
    public long getLikeId() {
        return likeId;
    }
    @JsonView({Views.PostLikeView.class})
    public byte getStatus() {
        return status;
    }
    @JsonView({Views.PostLikeView.class})
    public Date getTime() {
        return time;
    }
    @JsonView({Views.PostLikeView.class})
    public int getUser() {
        return user.getUserId();
    }
    @JsonView({Views.PostLikeView.class})
    public long getPost() {
        return post.getPostId();
    }
}
