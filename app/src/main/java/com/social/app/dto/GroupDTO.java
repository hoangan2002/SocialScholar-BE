package com.social.app.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.*;
import lombok.*;


import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GroupDTO {
    private long groupId;
    private String groupName;
    private String tags;
    private String description;
    private String imageURLGAvatar;
    private String imageUrlGCover;
    private Date timeCreate;
    private User hosts;
    private Category category;

    private Boolean isJoin=false;

    private String hashtag;

    private List<JoinManagement> joins;
    private List<Post> posts;


    @JsonView({Views.GroupsView.class, Views.GroupsView1.class})
    public long getGroupId(){return  groupId;}
    @JsonView({Views.GroupsView.class, Views.GroupsView1.class})
    public String getGroupName() {
        return groupName;
    }
    @JsonView({Views.GroupsView.class, Views.GroupsViewHashTag.class})
    public String getHashtag() {
        return tags;
    }
    @JsonView(Views.GroupsView.class)
    public String getDescription() {
        return description;
    }
    @JsonView({Views.GroupsView.class, Views.GroupsView1.class})
    public String getImageURLGAvatar() {
        return imageURLGAvatar;
    }
    @JsonView(Views.GroupsView.class)
    public String getImageUrlGCover() {
        return imageUrlGCover;
    }
    @JsonView({Views.GroupsView.class})
    public Date getTimeCreate() {
        return timeCreate;
    }
    @JsonView(Views.GroupsView.class)
    public String getHosts(){return hosts.getUserName();};
    @JsonView({Views.GroupsView.class, Views.GroupsViewSuggest.class})
    public String getCategory() { return category.getCategoryName();}


    @JsonView({Views.GroupsView.class, Views.GroupsView1.class})
    public Boolean getIsJoin() {
        return isJoin;
    }
    @JsonView(Views.GroupsView.class)
    public int getJoins() {
        return joins.size();
    }
    @JsonView(Views.GroupsView.class)
    public int getPosts() {
        return posts.size();
    }
}