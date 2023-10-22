package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Post;
import com.social.app.model.PostReportType;
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
public class PostReportDTO {
    private long reportId;
    private String description;
    private Date time;
    private PostReportType postReportType;
    private User user;
    private Post post;
    private boolean isChecked;
    @JsonView(Views.PostReportView.class)
    public long getReportId() {
        return reportId;
    }
    @JsonView(Views.PostReportView.class)
    public String getDescription() {
        return description;
    }
    @JsonView(Views.PostReportView.class)
    public Date getTime() {
        return time;
    }
    @JsonView(Views.PostReportView.class)
    public int getPostReportType() {
        return postReportType.getId();
    }
    @JsonView(Views.PostReportView.class)
    public int getUser() {
        return user.getUserId();
    }
    @JsonView(Views.PostReportView.class)
    public long getPost() {
        return post.getPostId();
    }
    @JsonView(Views.PostReportView.class)
    public boolean getIsChecked() {
        return isChecked;
    }
}
