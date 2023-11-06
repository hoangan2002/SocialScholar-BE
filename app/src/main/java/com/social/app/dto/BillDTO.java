package com.social.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.model.Document;
import com.social.app.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {

    private long billId;
    private Timestamp times;
    private Document document;
    private User user;
    private double price;

    @JsonView(Views.BillView.class)
    public long getBillId() {
        return billId;
    }

    @JsonView(Views.BillView.class)
    public Timestamp getTimes() {
        return times;
    }

    @JsonView(Views.BillView.class)
    public long getPrice() {
        return document.getCost();
    }
    @JsonView(Views.BillView.class)
    public long getDocument() {
        return document.getDocumentId();
    }
    @JsonView(Views.BillView.class)
    public int getUser() {
        return user.getActivityPoint();
    }

}