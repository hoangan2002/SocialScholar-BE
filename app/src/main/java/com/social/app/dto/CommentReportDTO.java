package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.*;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentReportDTO {
    private long reportId;
    private String description;
    private Date time;
    private CommentReportType commentReportType;
    private User user;
    private Comment comment;
    private boolean isChecked;

    @JsonView(Views.CommentReportView.class)
    public long getReportId() {
        return reportId;
    }
    @JsonView(Views.CommentReportView.class)
    public String getDescription() {
        return description;
    }
    @JsonView(Views.CommentReportView.class)
    public Date getTime() {
        return time;
    }
    @JsonView(Views.CommentReportView.class)
    public int getCommentReportType() {
        return commentReportType.getId();
    }
    @JsonView(Views.CommentReportView.class)
    public int getUser() {
        return user.getUserId();
    }
    @JsonView(Views.CommentReportView.class)
    public long getComment() {
        return comment.getCommentId();
    }
    @JsonView(Views.CommentReportView.class)
    public boolean getIsChecked() {
        return isChecked;
    }
}
