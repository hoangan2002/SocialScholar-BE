package com.social.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tagId;
    @ManyToOne
    @JoinColumn(name="groupId")
    private Groups group ;
    private String tagName;
}
