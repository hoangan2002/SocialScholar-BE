package com.social.app.entity;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest {
    private String documentName;
    private String description;
    private int cost;
    private Timestamp time;
}
