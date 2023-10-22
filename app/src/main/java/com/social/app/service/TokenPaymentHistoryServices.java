package com.social.app.service;

import com.social.app.model.Post;
import com.social.app.model.TokenPaymentHistory;
import com.social.app.repository.TokenPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Service
public class TokenPaymentHistoryServices {

    @Autowired
    TokenPaymentRepository tokenPaymentRepository;

    //đang dùng đơn vị tiền là usd nhé.
    public double calculateMoneyToBuy(long tokens){
        return tokens/20;
    }

    public TokenPaymentHistory saves(TokenPaymentHistory token){
        return tokenPaymentRepository.save(token);
    }

    public TokenPaymentHistory findPaymentId(String id){
        return tokenPaymentRepository.findByPaymentId(id);
    }

    public long countSuccessPayment(){
        long count =0;
        ArrayList<TokenPaymentHistory> tokenPaymentHistories = tokenPaymentRepository.findAll();
        for(TokenPaymentHistory t: tokenPaymentHistories){
            if(t.getStatus() == 1) count = count+1;
        }
        return count;
    }

    public double countProfit(){
        double profit =0;
        ArrayList<TokenPaymentHistory> tokenPaymentHistories = tokenPaymentRepository.findAll();
        for(TokenPaymentHistory t: tokenPaymentHistories){
            profit = profit+t.getAmountMoney();
        }
        return profit;
    }


}
