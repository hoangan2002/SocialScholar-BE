package com.social.app.request;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    long commentParentId;
    String content;
    Timestamp time;
}
