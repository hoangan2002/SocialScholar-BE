package com.social.app.entity;

import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Data {
    private String type;
    private String data;
}
