package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
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

    @JsonView(Views.UserView.class)
    public int getUserId(){ return userId; }
    @JsonView(Views.UserView.class)
    public String getUserName(){ return userName; }
    @JsonView(Views.UserView.class)
    public String getPhone(){ return phone; }
    @JsonView(Views.UserView.class)
    public String getPassword(){ return password; }
    @JsonView(Views.UserView.class)
    public String getEmail(){ return email; }
    @JsonView(Views.UserView.class)
    public String getAvatarURL(){ return avatarURL; }
    @JsonView(Views.UserView.class)
    public String getBackgroundURL(){ return backgroundURL; }
    @JsonView(Views.UserView.class)
    public String getLevel(){ return level; }
    @JsonView(Views.UserView.class)
    public String getRole(){ return role; }
    @JsonView(Views.UserView.class)
    public long getCoin(){ return coin; }
    @JsonView(Views.UserView.class)
    public int getActivityPoint(){ return activityPoint; }
    @JsonView(Views.UserView.class)
    public boolean getIsLocked(){ return isLocked; }



}
