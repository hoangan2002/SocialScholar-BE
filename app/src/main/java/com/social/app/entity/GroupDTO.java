package com.social.app.entity;


import com.social.app.model.Groups;
import lombok.*;


import java.util.Date;



@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    private long groupId;

    private String groupName;


    private String description;

    private String imageURLGAvatar;
    private String imageUrlGCover;

    private Date timeCreate;

    private String host;

    private String category;
    private String hashtag;

    public GroupDTO(Groups groups)
    {
//        this.category = groups.getCategory().getCategoryName();
        this.hashtag = groups.getTags().toString();
        this.groupId = groups.getGroupId();
        this.groupName = groups.getGroupName();
        this.description = groups.getDescription();
        this.imageURLGAvatar = groups.getImageURLGAvatar();
        this.imageUrlGCover = groups.getImageUrlGCover();
        this.timeCreate = groups.getTimeCreate();
//        this.host = groups.getHosts().getUserName();

    }

}
