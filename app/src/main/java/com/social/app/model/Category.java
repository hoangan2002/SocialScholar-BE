package com.social.app.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long categoryId;
    private String categoryName;
    @OneToMany(mappedBy = "categoryId")
    private List<Group> groups;
}
