package com.social.app.entity;

import lombok.*;

import java.util.Date;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponse {
    private long likeId;
    private byte status;
    private Date time;
    private String auth;
}
