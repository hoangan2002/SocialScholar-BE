package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "post_report_type")
public class PostReportType {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @JsonManagedReference(value = "postReport_type")
    @OneToMany(mappedBy = "postReportType")
    List<PostReport> postReports;
}
