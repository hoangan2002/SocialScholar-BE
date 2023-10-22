package com.social.app.repository;

import com.social.app.model.Bill;
import com.social.app.model.Document;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface BillRepository extends JpaRepository<Bill,Long> {
    Bill findByDocumentAndUser(Document doc, User user);
    public ArrayList<Bill> findByUser(User user);
}
