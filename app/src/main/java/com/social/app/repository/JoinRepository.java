package com.social.app.repository;

import com.social.app.model.Groups;
import com.social.app.model.JoinManagement;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface JoinRepository extends JpaRepository<JoinManagement, Long> {
    ArrayList<JoinManagement> findByGroup(Groups group);
    ArrayList<JoinManagement> findByUser(User user);
    JoinManagement save(JoinManagement joinManagement);


    void deleteByGroupAndUser(Groups groups, User user);
    void deleteByUser(User user);
}
