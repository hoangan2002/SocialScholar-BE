package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "Join_Management")
public class JoinManagement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long joinId;
    @JsonBackReference(value = "join_groups")
    @ManyToOne
    @JoinColumn(name="groupId")
    private Groups group;
    @JsonBackReference(value = "join_user")
    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    private Date time;
}
