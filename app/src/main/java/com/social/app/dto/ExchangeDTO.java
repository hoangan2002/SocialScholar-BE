package com.social.app.dto;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDTO {
    private Timestamp time;
    private long amountCoins;
    private double totalMoney;
}
