package com.social.app.repository;

import com.social.app.model.Document;
import com.social.app.model.Groups;
import com.social.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    Document save(Document document);
    ArrayList<Document> findAllByIsApprovedIsTrue();
    Document findFirstOrderByIsApprovedIsTrue();
    ArrayList<Document> findAllByIsApprovedIsFalse();
    ArrayList<Document> findByAuthorAndIsApprovedIsTrue(User user);
    ArrayList<Document> findByAuthorAndIsApprovedIsFalse(User user);
    ArrayList<Document> findByGroupAndIsApprovedIsTrue(Groups groups);
     ArrayList<Document> findByGroupAndIsApprovedIsFalse(Groups groups);
    ArrayList<Document> findAllByAuthor(User author);
     Document findByDocumentId(long documentId);
     void deleteById(long id);

    public void deleteByAuthor(User author);

    @Query(value = "SELECT * FROM document WHERE MATCH (documentName) AGAINST (?1) > 0 AND isApproved = 1 ORDER BY MATCH (documentName) AGAINST (?1) DESC", nativeQuery = true)
    ArrayList<Document> fullTextSearch(String keyword);
//  AGAINST (?1 WITH QUERY EXPANSION)
}
