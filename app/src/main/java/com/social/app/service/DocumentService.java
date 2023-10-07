package com.social.app.service;

import com.social.app.model.Bill;
import com.social.app.model.Document;
import com.social.app.model.Groups;
import com.social.app.model.User;
import com.social.app.repository.BillRepository;
import com.social.app.repository.DocumentRepository;
import com.social.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BillRepository billRepository;

    @Transactional
    public boolean  DocumentExchangeTransaction(User customer, Document document){
        int docCost = document.getCost();
        long uCoin = customer.getCoin();
        if (uCoin < docCost) throw  new RuntimeException("Not Enough Coins");

        // update coin cho 2 user
        User author = document.getAuthor();
        long athCoin = author.getCoin();
        customer.setCoin(uCoin-docCost);
        author.setCoin(athCoin+docCost);

        // Tao bill
        Bill bill = new Bill();
        bill.setDocument(document);
        bill.setUser(customer);
        Date date = new Date();
        long time = date.getTime();
        Timestamp datetime = new Timestamp(time);
        bill.setTime(datetime);

        // Luu databse
        userRepository.save(customer);
        userRepository.save(author);
        billRepository.save(bill);

        return true;
    }

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
    public ArrayList<Document> GroupApprovedDocuments(Groups groups){
        return documentRepository.findByGroupAndIsApprovedIsTrue(groups);
    }

    public Document findDocumentbyId(long id){
        return documentRepository.findByDocumentId(id);
    }

    public String deleteDocumentById(long id){
        documentRepository.deleteById(id);
        return "success";
    }
}
