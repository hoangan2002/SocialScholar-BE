package com.social.app.repository;

import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByName(String username);
    Optional<User> findByEmail(String email);
}
