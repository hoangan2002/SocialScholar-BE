package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Groups_")
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="Groups_ID")
    private long groupId;

    @Column(name="Groups_Name")
    private String groupName;


    @Column(name="Descriptions")
    private String description;

    @Column(name="Create_Time")
    private Date timeCreate;

    @ManyToOne
    @JoinColumn(name="user_Id")
    private User hosts;

    @ManyToOne
    @JoinColumn(name="category_Id")
    private Category category;
//
    @OneToMany(mappedBy = "group")
    private List<Post> posts;

    @OneToMany(mappedBy = "group")
    private List<Tag> tags;

    @OneToMany(mappedBy = "group")
    private  List<JoinManagement> joins;

    @OneToMany(mappedBy = "group")
    private  List<Document> documents;

}