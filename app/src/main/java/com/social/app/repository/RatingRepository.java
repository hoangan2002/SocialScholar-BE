package com.social.app.repository;

import com.social.app.model.Rating;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    public Rating findByUser(User user);
}
