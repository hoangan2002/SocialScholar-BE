package com.social.app.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ratingId;
    private int stars;
    private String description;
    private Date time;

    @JsonBackReference(value = "rating_document")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="document_Id")
    private Document document;

    @JsonBackReference(value = "rating_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private  User user;

}
