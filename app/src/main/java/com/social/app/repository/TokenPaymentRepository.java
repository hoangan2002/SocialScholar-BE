package com.social.app.repository;

import com.social.app.model.Post;
import com.social.app.model.TokenPaymentHistory;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TokenPaymentRepository extends JpaRepository<TokenPaymentHistory,Long> {
    TokenPaymentHistory save(TokenPaymentHistory tokenPaymentHistory);

    TokenPaymentHistory findByPaymentId(String paymentId);
    ArrayList<TokenPaymentHistory> findAll();
    public void deleteByUser(User user);
}
