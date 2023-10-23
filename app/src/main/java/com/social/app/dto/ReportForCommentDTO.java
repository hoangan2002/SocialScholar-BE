package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.CommentReportType;
import com.social.app.model.Post;
import com.social.app.model.PostReportType;
import com.social.app.model.User;
import lombok.*;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportForCommentDTO {
    private long reportId;
    private String description;
    private Date time;
    private CommentReportType commentReportType;
    private User user;
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
    public int getCommentReportType() {
        return commentReportType.getId();
    }
    @JsonView(Views.PostReportView.class)
    public int getUser() {
        return user.getUserId();
    }
    @JsonView(Views.PostReportView.class)
    public boolean getIsChecked() {
        return isChecked;
    }
}
