package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Category")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int categoryId;
    private String categoryName;
    private String description;

    @JsonManagedReference(value = "category_groups")
    @OneToMany(mappedBy = "category")
    private List<Groups> groups;

    @JsonManagedReference(value = "category_hintTag")
    @OneToMany(mappedBy = "category")
    private List<HintTag> hintTags;

    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    public Category(){

    }

    @Override
    public String toString() {
        return "Category: " + categoryId +
                " | " + categoryName;
    }
}
