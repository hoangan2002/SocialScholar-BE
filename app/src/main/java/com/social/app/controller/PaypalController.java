package com.social.app.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.social.app.model.TokenPaymentHistory;
import com.social.app.model.User;
import com.social.app.service.PaypalService;
import com.social.app.service.TokenPaymentHistoryServices;
import com.social.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Timestamp;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/paypal")
public class PaypalController {

    private final PaypalService paypalService;
    @Autowired
     TokenPaymentHistoryServices tokenPaymentHistoryServices;
    @Autowired
    UserService userService;

//    PayPal Customer test account
//            Email: sb-9vmaj25734596@personal.example.com
//            pass: 4D<0?<p8

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @PostMapping("/payment/create")
    public RedirectView createPayment(
            @RequestParam("method") String method,
            @RequestParam("amount") String amount,
            @RequestParam("currency") String currency,
            @RequestParam("description") String description
    ) {
        TokenPaymentHistory tokenPaymentHistory = new TokenPaymentHistory();
        try {
            String cancelUrl = "http://localhost:8080/paypal/payment/cancel";
            String successUrl = "http://localhost:8080/paypal/payment/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(amount),
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            int userid = userService.findUserByUsername(authentication.getName()).getUserId();
            int userid=1;
            double amounts = Double.parseDouble(amount);
            tokenPaymentHistory.setAmountMoney(amounts);
            Date date = new Date();
            long time = date.getTime();
            Timestamp datetime = new Timestamp(time);
            tokenPaymentHistory.setTime(datetime);
            tokenPaymentHistory.setStatus((byte) 0);
            tokenPaymentHistory.setUser(userService.findById(userid));
            tokenPaymentHistory.setAmountToken(Long.parseLong(amount)*20);

            tokenPaymentHistoryServices.saves(tokenPaymentHistory);

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    tokenPaymentHistory.setPaymentId(payment.getId());
                    tokenPaymentHistoryServices.saves(tokenPaymentHistory);
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            tokenPaymentHistory.setStatus((byte)0);
            tokenPaymentHistoryServices.saves(tokenPaymentHistory);
            log.error("Error occurred:: ", e);
        }
        return new RedirectView("/payment/error");
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                TokenPaymentHistory tokenPaymentHistory = tokenPaymentHistoryServices.findPaymentId(payment.getId());
                tokenPaymentHistory.setStatus((byte)1);
                tokenPaymentHistoryServices.saves(tokenPaymentHistory);
                User user = userService.findById(tokenPaymentHistory.getUser().getUserId());
                user.setCoin(user.getCoin()+tokenPaymentHistory.getAmountToken());
                userService.save(user);
                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
        }
        return "paymentSuccess";
    }

    @GetMapping("/payment/cancel")
    public String paymentCancel() {
        return "paymentCancel";
    }

    @GetMapping("/payment/error")
    public String paymentError() {
        return "paymentError";
    }
}
