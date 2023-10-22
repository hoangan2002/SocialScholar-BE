package com.social.app.controller;

import com.social.app.model.TokenPaymentHistory;
import com.social.app.model.User;
import com.social.app.service.TokenPaymentHistoryServices;
import com.social.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;
import java.util.*;

@RestController
public class PayPalController {

    @Autowired
    TokenPaymentHistoryServices tokenPaymentHistoryServices;
    @Autowired
    UserService userService;

    //    PayPal Customer test account
//            Email: sb-9vmaj25734596@personal.example.com
//            pass: 4D<0?<p8
    private final String  BASE = "https://api-m.sandbox.paypal.com";
    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private String getAuth(String client_id, String app_secret) {
        String auth = client_id + ":" + app_secret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }

    public String generateAccessToken() {
        String auth = this.getAuth(
                "AfjeUBEZZPAGm0u1Wlz6t6V_W4Xkq0KpzZc4UeyOqfh3aVCkYCUlOwGnxhlkeHqhHg3q6IrjP_qMl2yC",
                "ENbOoXy9jGtjdAP_WyU9v_W4nn-UDyCQj6FPEScvrjxukZwGpoVB7lg-bDV7ztUNRs3fLyxa58Trtz4-"


        );
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE +"/v1/oauth2/token",
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.log(Level.INFO, "GET TOKEN: SUCCESSFUL!");
            return new JSONObject(response.getBody()).getString("access_token");
        } else {
            LOGGER.log(Level.SEVERE, "GET TOKEN: FAILED!");
            return "Unavailable to get ACCESS TOKEN, STATUS CODE " + response.getStatusCode();
        }
    }

    @RequestMapping(value="/api/orders/{orderId}/capture", method = RequestMethod.POST)
    @CrossOrigin
    public Object capturePayment(@PathVariable("orderId") String orderId) {
        String accessToken = generateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders/" + orderId + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CREATED");
            TokenPaymentHistory tokenPaymentHistory = tokenPaymentHistoryServices.findPaymentId(orderId);
            tokenPaymentHistory.setStatus((byte)1);
            tokenPaymentHistoryServices.saves(tokenPaymentHistory);
            User user = userService.findById(tokenPaymentHistory.getUser().getUserId());
            user.setCoin(user.getCoin()+tokenPaymentHistory.getAmountToken());
            userService.save(user);
            return response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }
    }

    @RequestMapping(value="/api/orders", method = RequestMethod.POST)
    @CrossOrigin
    public Object createOrder(@RequestParam double total, @RequestParam int userId) {
        String accessToken = generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //JSON String
        String requestJson = "{\"intent\":\"CAPTURE\",\"purchase_units\":[{\"amount\":{\"currency_code\":\"USD\",\"value\":\""+total+"\"}}]}";
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders",
                HttpMethod.POST,
                entity,
                Object.class
        );
        TokenPaymentHistory tokenPaymentHistory = new TokenPaymentHistory();
        tokenPaymentHistory.setAmountMoney(total);
        Date date = new Date();
        long time = date.getTime();
        Timestamp datetime = new Timestamp(time);
        tokenPaymentHistory.setTime(datetime);
        tokenPaymentHistory.setStatus((byte) 0);
        tokenPaymentHistory.setUser(userService.findById(userId));
        tokenPaymentHistory.setAmountToken(Math.round(total)*20);
        tokenPaymentHistoryServices.saves(tokenPaymentHistory);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CAPTURE");
            Object s = response.getBody();
            tokenPaymentHistory.setPaymentId( s.toString().substring(4,21));
            tokenPaymentHistoryServices.saves(tokenPaymentHistory);
            return response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CAPTURING ORDER");
            return "Unavailable to get CAPTURE ORDER, STATUS CODE " + response.getStatusCode();
        }

    }
}
