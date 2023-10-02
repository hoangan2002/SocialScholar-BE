package com.social.app.entity;
import com.social.app.model.PostLike;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private long postId;
    private String content;
    private Timestamp time;
    private String titles;
    private String imageURL;
    private String author;
    private String groupName;
    private List<CommentResponse> comments;
    private List<LikeResponse> likes;
}
