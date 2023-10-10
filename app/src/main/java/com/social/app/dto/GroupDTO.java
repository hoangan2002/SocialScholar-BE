package com.social.app.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Category;
import com.social.app.model.Groups;
import com.social.app.model.User;
import lombok.*;


import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDTO {
    private long groupId;
    private String groupName;
    private String description;
    private String imageURLGAvatar;
    private String imageUrlGCover;
    private Date timeCreate;
    private User hosts;
    private Category category;
    private String hashtag;
    private Boolean isJoin;

    @JsonView(Views.GroupsView.class)
    public long getGroupId(){return  groupId;}
    @JsonView(Views.GroupsView.class)
    public String getHosts(){return "hoangvh238.dev";};
    @JsonView(Views.GroupsView.class)
    public String getGroupName() {
        return groupName;
    }
    @JsonView(Views.GroupsView.class)
    public String getDescription() {
        return description;
    }
    @JsonView(Views.GroupsView.class)
    public String getImageUrlGCover() {
        return imageUrlGCover;
    }
    @JsonView(Views.GroupsView.class)
    public Date getTimeCreate() {
        return timeCreate;
    }

    @JsonView(Views.GroupsView.class)
    public String getCategory() {
        return "Java, Java serverlet";
    }
    @JsonView(Views.GroupsView.class)
    public String getHashtag() {
        return hashtag;
    }
    @JsonView(Views.GroupsView.class)
    public Boolean getIsJoin() {
        return isJoin;
    }
}