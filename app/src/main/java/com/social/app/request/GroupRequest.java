package com.social.app.request;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupRequest {
    String description;
    String groupName;
    Timestamp timeCreate;
    String tags;
    int categoryId ;
}
