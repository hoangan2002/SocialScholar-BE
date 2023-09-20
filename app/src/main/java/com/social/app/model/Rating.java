package com.social.app.model;

import com.social.app.controller.DashboardController;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

public class Rating {
    private long ratingId;
    private byte stars;
    private String description;
    private Date time;

    @ManyToOne
    @JoinColumn(name="documentId")
    private Document document;

    @ManyToOne
    @JoinColumn(name="userId")
    private  User user;

}
