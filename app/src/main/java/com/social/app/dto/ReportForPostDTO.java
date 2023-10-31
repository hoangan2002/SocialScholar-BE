package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
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
public class ReportForPostDTO {
    private long reportId;
    private String description;
    private Date time;
    private PostReportType postReportType;
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
    public String getPostReportType() {
        return postReportType.getName();
    }
    @JsonView(Views.PostReportView.class)
    public int getUser() {
        return user.getUserId();
    }
    @JsonView(Views.PostReportView.class)
    public String getUserName() {
        return user.getUserName();
    }
    @JsonView(Views.PostReportView.class)
    public String getUserAvatar() {
        return user.getAvatarURL();
    }
    @JsonView(Views.PostReportView.class)
    public boolean getIsChecked() {
        return isChecked;
    }
}
