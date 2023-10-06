package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Exchange_Request")
public class ExchangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long requestId;
    private Timestamp time;
    private long amountCoins;
    private double totalMoney;
    private byte status;

    @JsonBackReference(value = "exchange_user")
    @ManyToOne
    @JoinColumn(name="user_Id")
    private User user;

}
