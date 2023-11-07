package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Bill;
import com.social.app.model.Groups;
import com.social.app.model.Rating;
import com.social.app.model.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private long documentId;
    private String documentName;
    private String description;
    private int cost;
    private Timestamp time;
    private Groups group;
    private User author;
    private String message;
    private List<RatingDTO> ratings;
    private List<Bill> bills;
    @JsonView(Views.DocumentView.class)
    public long getDocumentId(){ return documentId; }
    @JsonView(Views.DocumentView.class)
    public String getDocumentName(){ return documentName; }
    @JsonView(Views.DocumentView.class)
    public String getDescription(){ return description; }
    @JsonView(Views.DocumentView.class)
    public int getCost(){ return cost; }
    @JsonView(Views.DocumentView.class)
    public Timestamp getTime(){ return time; }
    @JsonView(Views.DocumentView.class)
    public String getGroup(){ return group.getGroupName(); }
    @JsonView(Views.DocumentView.class)
    public String getAuthor(){ return author.getUserName(); }
    @JsonView(Views.DocumentView.class)
    public String getMessage(){ return message; }
//    @JsonView(Views.UserView.class)
//    public int[] getRatings(){
//        int[] userRates = new int[ratings.size()];
//        int i=0;
//        for(Rating rating: ratings){
//            userRates[i] = rating.getUser().getUserId();
//            i++;
//        };
//        return userRates;
//    }
    @JsonView(Views.DocumentView.class)
    public List<RatingDTO> getRatings(){ return ratings; }
    @JsonView(Views.DocumentView.class)
    public int getAuthorPoints(){ return author.getActivityPoint(); }
    @JsonView(Views.DocumentView.class)
    public int getBills(){ return bills.size(); }
}
