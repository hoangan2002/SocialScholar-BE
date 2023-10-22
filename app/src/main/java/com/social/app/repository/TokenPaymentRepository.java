package com.social.app.repository;

import com.social.app.model.Post;
import com.social.app.model.TokenPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenPaymentRepository extends JpaRepository<TokenPaymentHistory,Long> {
    TokenPaymentHistory save(TokenPaymentHistory tokenPaymentHistory);

    TokenPaymentHistory findByPaymentId(String paymentId);
}
