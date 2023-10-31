package com.social.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;



@Service
@Scope("prototype")
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JwtService jwtService;
    public int code;
    //Gửi mail để xác minh otp resetPassword
    public void sendEmail(String toEmail){
        Random random = new Random();
        code = random.nextInt(99999 - 10000 + 1) + 10000;
        System.out.println("Code: "+ code);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("antdhde160073@fpt.edu.vn");
        message.setTo(toEmail);
        message.setText("Hey "+toEmail+",Your Reset Code: " + code +". Please fill in this code");
        message.setSubject("Reset Password CampSchoolar");
        mailSender.send(message);
        System.out.println("Mail haved send");

    }
    //Gửi mail để xác minh email
    public void sendEmailVerify(String toEmail){
        Random random = new Random();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("antdhde160073@fpt.edu.vn");
        message.setTo(toEmail);
        String link = "http://localhost:300/authentication/"+ jwtService.generateTokenEmail(toEmail);
        message.setText("Your Verify Link: "+link);
        message.setSubject("Verify Email CampSchoolar");
        mailSender.send(message);
        System.out.println("Mail haved send");

    }
}