package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Post;
import com.social.app.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostSaveDTO {
    private long saveId;
    private Timestamp time;
    private User user;
    private Post post;

    @JsonView(Views.PostView.class)
    public long getSaveId(){ return saveId; }
    @JsonView(Views.PostView.class)
    public Timestamp getTime(){ return time; }
    @JsonView(Views.PostView.class)
    public String getUser(){ return user.getUserName(); }
    @JsonView(Views.PostView.class)
    public Post getPost(){ return post; }
}
