package com.social.app.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountResponse {

    private ArrayList<String> days;
    private Data data;

}
