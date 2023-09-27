package com.social.app.model;
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


    @JsonManagedReference(value = "post_groups")
    @OneToMany(mappedBy = "group")
    private List<Post> posts;

    @OneToMany(mappedBy = "group")
    private List<Tag> tags;

    @JsonManagedReference(value = "join_groups")
    @OneToMany(mappedBy = "group")
    private  List<JoinManagement> joins;

    @OneToMany(mappedBy = "group")
    private  List<Document> documents;

}
