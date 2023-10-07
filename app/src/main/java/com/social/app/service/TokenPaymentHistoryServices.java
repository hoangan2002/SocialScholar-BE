package com.social.app.service;

public class TokenPaymentHistoryServices {

    //đang dùng đơn vị tiền là usd nhé.
    public double calculateMoneyToBuy(long tokens){
        return tokens/20;
    }

}
