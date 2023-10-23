package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Tags")
public class    HintTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int tagId;
    private String tagName;
    @ManyToOne
    @JoinColumn(name="category_Id")
    @JsonBackReference(value = "category_hintTag")
    private Category category;

    public HintTag(String tagName, Category category) {
        this.tagName = tagName;
        this.category = category;
    }

    public HintTag() {
    }

    @Override
    public String toString() {
        return "HintTag: " + tagId +
                " | " + tagName;
    }
}
