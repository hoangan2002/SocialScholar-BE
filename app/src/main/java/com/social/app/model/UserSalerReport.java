package com.social.app.model;

import com.social.app.dto.BillDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserSalerReport {
    private String groupName;
    private String documentName;
    private long documentId;
    private ArrayList<BillDTO> billReport;
    private double revenue;


}