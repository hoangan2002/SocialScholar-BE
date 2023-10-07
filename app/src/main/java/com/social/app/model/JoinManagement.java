package com.social.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    public JoinManagement(Groups groups, User user, Date time) {
        this.group=groups;
        this.user=user;
        this.time=time;
    }
}
