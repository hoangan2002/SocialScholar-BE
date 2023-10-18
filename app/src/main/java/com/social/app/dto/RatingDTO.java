package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Document;
import com.social.app.model.User;
import lombok.*;

import java.util.Date;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    private long ratingId;
    private int stars;
    private String description;
    private Date time;
    private Document document;
    private User user;


    @JsonView({Views.RatingView.class})
    public long getRatingId() {
        return ratingId;
    }
    @JsonView({Views.RatingView.class})
    public int getStars() {
        return stars;
    }
    @JsonView({Views.RatingView.class})
    public String getDescription() {
        return description;
    }
    @JsonView({Views.RatingView.class})
    public Date getTime() {
        return time;
    }
    @JsonView({Views.RatingView.class})
    public long getDocument() {
        return document.getDocumentId();
    }
    @JsonView({Views.RatingView.class})
    public int getUser() {
        return user.getUserId();
    }

}
