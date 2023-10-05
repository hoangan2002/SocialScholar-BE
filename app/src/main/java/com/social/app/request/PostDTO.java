package com.social.app.request;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    String content ;
    Timestamp time;
    int userId;
    int groupId;
    String title ;

}
