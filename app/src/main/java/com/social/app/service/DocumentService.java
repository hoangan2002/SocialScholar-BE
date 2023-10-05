package com.social.app.service;

import com.social.app.model.Document;
import com.social.app.model.User;
import com.social.app.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    public Document saveNew(Document document) {
        Date date = new Date();
        long time = date.getTime();
        Timestamp datetime = new Timestamp(time);
        document.setTime(datetime);
        return documentRepository.save(document);
    }
    public Document update(Document document){
        return documentRepository.save(document);
    }

    public ArrayList<Document> allApprovedDocuments(){
        return documentRepository.findAllByIsApprovedIsTrue();
    }
    public ArrayList<Document> allUnApprovedDocuments(){
        return documentRepository.findAllByIsApprovedIsFalse();
    }
    public ArrayList<Document> UserApprovedCreatedDocuments(User user){
        return documentRepository.findByAuthorAndIsApprovedIsTrue(user);
    }

    public Document findDocumentbyId(long id){
        return documentRepository.findByDocumentId(id);
    }

    public String deleteDocumentById(long id){
        documentRepository.deleteById(id);
        return "success";
    }
}
