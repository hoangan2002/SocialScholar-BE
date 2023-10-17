package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
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
    private List<Rating> ratings;
    @JsonView(Views.UserView.class)
    public long getDocumentId(){ return documentId; }
    @JsonView(Views.UserView.class)
    public String getDocumentName(){ return documentName; }
    @JsonView(Views.UserView.class)
    public String getDescription(){ return description; }
    @JsonView(Views.UserView.class)
    public int getCost(){ return cost; }
    @JsonView(Views.UserView.class)
    public Timestamp getTime(){ return time; }
    @JsonView(Views.UserView.class)
    public String getGroup(){ return group.getGroupName(); }
    @JsonView(Views.UserView.class)
    public String getAuthor(){ return author.getUserName(); }
    @JsonView(Views.UserView.class)
    public List<Rating> getRatings(){ return ratings; }
}
