package com.social.app.repository;

import com.social.app.model.Document;
import com.social.app.model.Groups;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    Document save(Document document);
    ArrayList<Document> findAllByIsApprovedIsTrue();
    ArrayList<Document> findAllByIsApprovedIsFalse();
    ArrayList<Document> findByAuthorAndIsApprovedIsTrue(User user);
    ArrayList<Document> findByGroupAndIsApprovedIsTrue(Groups groups);

     Document findByDocumentId(long documentId);
     void deleteById(long id);

}
