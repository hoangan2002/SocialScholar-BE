package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.model.ExchangeRequest;
import com.social.app.model.User;
import com.social.app.service.ExchangeRequestServices;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exchange-request")
public class ExchangeRequestController {

    @Autowired
    UserService userService;

    @Autowired
    ExchangeRequestServices exchangeRequestServices;

    @PreAuthorize("isAuthenticated() and hasRole('ROLE_USER')")
    @PostMapping("/make-request")
    public ResponseEntity<ResponseObject> makeRequest(@RequestPart ExchangeRequest requests){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userid = userService.findUserByUsername(authentication.getName()).getUserId();
        if(requests.getAmountCoins() > userService.loadUserById(userid).getCoin()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed","Not enough Coins","")
            );
        }else {
            if(requests.getAmountCoins()<30)  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed","Coin must larger 30","")
            );
            ExchangeRequest exchangeRequest = new ExchangeRequest();
            exchangeRequest.setUser(userService.loadUserById(userid));

            User user = userService.loadUserById(userid);
            user.setCoin(user.getCoin()-requests.getAmountCoins());
            userService.save(user);

            exchangeRequest.setStatus((byte)0);
            exchangeRequest.setAmountCoins(requests.getAmountCoins());
            exchangeRequest.setTotalMoney(requests.getTotalMoney());
            exchangeRequestServices.submitRequest(exchangeRequest);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK","Submit success",exchangeRequest)
            );
        }
    }
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @PostMapping("check-request/{requestid}")
    public ResponseEntity<ResponseObject> checkRequest(@PathVariable("requestid") long id, @RequestParam byte status){
        ExchangeRequest e = exchangeRequestServices.loadRequestById(id);
        if (e==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("Failed","Can't find the request","")
        );

        if(status==0){
            User user = userService.loadUserById(e.getUser().getUserId());
            user.setCoin(user.getCoin()+ e.getAmountCoins());
            userService.save(user);
            e.setStatus(status);
        }else{
            e.setStatus(status);
        }
        e.setStatus(status);
        exchangeRequestServices.submitRequest(e);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK","Update status success",e)
        );
    }
}
