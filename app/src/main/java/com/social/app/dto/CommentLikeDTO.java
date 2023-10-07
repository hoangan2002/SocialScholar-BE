package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Comment;
import com.social.app.model.Post;
import com.social.app.model.User;
import lombok.*;

import java.util.Date;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentLikeDTO {
    private long likeId;
    private byte status;
    private Date time;
    private User user;
    private Comment comment;

    @JsonView({Views.CommentLikeView.class})
    public long getLikeId() {
        return likeId;
    }
    @JsonView({Views.CommentLikeView.class})
    public byte getStatus() {
        return status;
    }
    @JsonView({Views.CommentLikeView.class})
    public Date getTime() {
        return time;
    }
    @JsonView({Views.CommentLikeView.class})
    public int getUser() {
        return user.getUserId();
    }
    @JsonView({Views.CommentLikeView.class})
    public long getComment() {
        return comment.getCommentId();
    }
}
