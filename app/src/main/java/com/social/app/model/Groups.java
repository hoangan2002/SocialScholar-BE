package com.social.app.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;


import java.util.Date;
import java.util.List;

@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "Groups_")
public class Groups {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name="Groups_ID")
    private long groupId;

    @Column(name="Groups_Name")
    private String groupName;

    private String tags;

    @Column(name="Descriptions")
    private String description;

    @Column(name="ImageUrlGAvatar")
    private String imageURLGAvatar;
    @Column(name="ImageUrlGCover")
    private String imageUrlGCover;

    @Column(name="Create_Time")
    private Date timeCreate;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="host_Id")
    private User hosts;

    @ManyToOne
    @JoinColumn(name="category_Id")
    @JsonBackReference(value = "category_groups")
    private Category category;


    @JsonManagedReference(value = "post_groups")
    @OneToMany(mappedBy = "group")
    private List<Post> posts;

    @JsonManagedReference(value = "join_groups")
    @OneToMany(mappedBy = "group")
    private  List<JoinManagement> joins;

    @OneToMany(mappedBy = "group")
    private  List<Document> documents;

}
