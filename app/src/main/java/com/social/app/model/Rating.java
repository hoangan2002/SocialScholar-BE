package com.social.app.model;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long ratingId;
    private byte stars;
    private String description;
    private Date time;

    @ManyToOne
    @JoinColumn(name="document_Id")
    private Document document;

    @ManyToOne
    @JoinColumn(name="user_Id")
    private  User user;

}
