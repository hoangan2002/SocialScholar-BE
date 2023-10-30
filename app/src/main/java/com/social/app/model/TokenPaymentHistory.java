package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Token_Payment_History")
public class TokenPaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long toHistoryId;
    private String paymentId;
    private long amountToken;
    private double amountMoney;
    private Timestamp time;

    private byte status;

    @ManyToOne
    @JoinColumn(name="userId")
    @JsonBackReference(value = "payment_user")
    private User user;


}
