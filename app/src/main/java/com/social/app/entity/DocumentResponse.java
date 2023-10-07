package com.social.app.entity;

import com.social.app.model.Groups;
import com.social.app.model.Rating;
import com.social.app.model.User;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {
    private long documentId;
    private String documentName;
    private String description;
    private int cost;
    private Timestamp time;
    private List<Rating> ratings;
    private String groupName;
    private String author;
}
