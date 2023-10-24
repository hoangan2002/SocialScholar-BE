package com.social.app.repository;

import com.social.app.model.ExchangeRequest;
import com.social.app.model.Post;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest,Long> {
    ExchangeRequest save(ExchangeRequest exchangeRequest);
    ExchangeRequest findByRequestId(long requestId);
    ArrayList<ExchangeRequest> findAll();
    public void deleteByUser(User user);
}
