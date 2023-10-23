package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.service.TokenPaymentHistoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment-history")
public class TokenPaymentController {

    @Autowired
    TokenPaymentHistoryServices tokenPaymentHistoryServices;

    @GetMapping("/get-sucess-payment")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> countDonePayment(){
        long result = tokenPaymentHistoryServices.countSuccessPayment();
        if(result==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Empty", "No payment Success", ""));
        }else  return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Count Success", result));
    }

    @GetMapping("/get-profit")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> countProfit(){
        double result = tokenPaymentHistoryServices.countProfit();
        if(result==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Empty", "No payment Success", ""));
        }else  return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Count Success", result));
    }
}
