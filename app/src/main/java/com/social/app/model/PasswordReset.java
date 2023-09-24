package com.social.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name= "passwordreset")
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private int code;


    public PasswordReset(String email, int code) {
        this.email=email;
        this.code=code;
    }




}