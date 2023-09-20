package com.social.app.model;

import jakarta.persistence.*;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tagId;
    @ManyToOne
    @JoinColumn(name="groupId;")
    private Group group ;
    private String tagName;
}
