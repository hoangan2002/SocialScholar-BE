package com.social.app.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long groupId;
    private String groupName;
    @ManyToOne
    @JoinColumn(name="userId")
    private User host;
    private String description;
    private Date timeCreate;
//    @ManyToOne
//    @JoinColumn(name="categoryId")
//    private Category category;
//
//    @OneToMany(mappedBy = "group")
//    private List<Post> posts;
//    @OneToMany(mappedBy = "group")
//    private List<Tag> tags;

//    @OneToMany(mappedBy = "group")
//    private  List<JoinManagement> joins;
//
//    @OneToMany(mappedBy = "group")
//    private  List<Document> documents;

}
