package com.social.app.repository;

import com.social.app.model.Groups;
import com.social.app.model.JoinManagement;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(int id);
    public User findUserByUserName(String username);
    public  ArrayList<User> findAll();

//    User findByJoins(JoinManagement joins);
}
