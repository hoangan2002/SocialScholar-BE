package com.social.app.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Category")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long categoryId;
    private String categoryName;
    @OneToMany(mappedBy = "category")
    private List<Groups> groups;
}
