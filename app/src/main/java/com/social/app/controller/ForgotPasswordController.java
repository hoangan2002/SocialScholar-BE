package com.social.app.controller;

import com.social.app.entity.ResponseObject;
import com.social.app.model.PasswordReset;
import com.social.app.model.User;
import com.social.app.repository.PasswordResetDAO;
import com.social.app.service.EmailSenderService;
import com.social.app.service.JwtService;
import com.social.app.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/forgot")
public class ForgotPasswordController {
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserService service;
    @Autowired
    private PasswordResetDAO passwordResetDAO;
    @Autowired
    private JwtService jwtService;
    @GetMapping
    public String forgot() { return "forgot"; }

    @GetMapping("/a")

    public ResponseEntity<ResponseObject> reset(@RequestParam("email") String email){
        System.out.println(email);
        User user = new User();
        user.setEmail(email);
        //Kiểm tra email có trên hệ thống
        if(!service.isExits(user)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Email not exists", "ERROR",null));
        }
        //Kiểm tra email đã có mã otp,
        if(passwordResetDAO.findByEmail(email) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("OTP had been send, Please come back after 5 minutes", "ERROR",null));
        }

        emailSenderService.sendEmail(email);
        passwordResetDAO.save(new PasswordReset(email,emailSenderService.code));
         return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Email had been send", "OK",null));
    }
    @GetMapping("/verifykey")
    public ResponseEntity<ResponseObject> verifykey(@RequestParam("otp") int otp, @RequestParam("email") String email ){
        if(passwordResetDAO.findByEmail(email). getCode() == otp)
        {return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OTP is correct", "OK",jwtService.generateTokenOTP(passwordResetDAO.findByEmail(email))));}
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("OTP is incorrect", "ERROR",null));
    }
    @Transactional
    @GetMapping("/verifykey/{token}")
    public ResponseEntity<ResponseObject> setpw(@RequestParam("password") String password,@PathVariable String token) {

        Map<String,Object> claims = jwtService.validateTokenOtp(token);
            String email = claims.get("email").toString();

            //kiểm tra email  lấy được có khớp
            if (passwordResetDAO.isExist(email)){
                passwordResetDAO.delete(passwordResetDAO.findByEmail(email));
                service.updatePassword(email,password);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Reset Successfull", "OK",null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Reset Fail", "OK",null));




    }
}