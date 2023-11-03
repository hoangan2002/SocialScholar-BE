package com.social.app.service;

import com.social.app.model.ExchangeRequest;
import com.social.app.model.Post;
import com.social.app.repository.ExchangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Service
public class ExchangeRequestServices {

    @Autowired
    ExchangeRequestRepository exchangeRequestRepository;

    public ExchangeRequest submitRequest(ExchangeRequest exchangeRequest){
        Date date = new Date();
        long time = date.getTime();
        Timestamp datetime = new Timestamp(time);
        exchangeRequest.setTime(datetime);
        exchangeRequest.setTotalMoney(exchangeRequest.getAmountCoins()/30);
        exchangeRequest.setStatus((byte)1);
        return exchangeRequestRepository.save(exchangeRequest);
    }

    public ArrayList<ExchangeRequest> retriveExchangeRequestFromDB(){
        ArrayList<ExchangeRequest> result = exchangeRequestRepository.findAll();
        return result;  
    }

    public ExchangeRequest loadRequestById(long requestId) {
        ExchangeRequest exchangeRequest = exchangeRequestRepository.findByRequestId(requestId);
        if(exchangeRequest!=null)
            return exchangeRequest;
        else return null;
    }

}
