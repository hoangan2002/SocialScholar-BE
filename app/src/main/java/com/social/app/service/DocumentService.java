package com.social.app.service;

import com.social.app.dto.DocumentDTO;
import com.social.app.dto.UserDTO;
import com.social.app.model.Bill;
import com.social.app.model.Document;
import com.social.app.model.Groups;
import com.social.app.model.User;
import com.social.app.repository.BillRepository;
import com.social.app.repository.DocumentRepository;
import com.social.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    ModelMapper modelMapper;

    public DocumentDTO MapDocumentDTO(Document document){
        DocumentDTO documentDTO = modelMapper.map(document,DocumentDTO.class);
        return documentDTO;
    }

    public ArrayList<DocumentDTO> ListDocumentDTO(ArrayList<Document> documents){
        ArrayList<DocumentDTO> documentDTOS = new ArrayList<>();
        for (Document item:
            documents ) {
            documentDTOS.add(MapDocumentDTO(item));
        }
        return documentDTOS;
    }

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
    public ArrayList<Document> BoughtDocuments(User user){
        ArrayList<Bill> myBills = billRepository.findByUser(user);
        ArrayList<Document> result = new ArrayList<>();
        for (Bill bill: myBills){
            result.add(bill.getDocument());
        }
        return result;
    }

    public Document findDocumentbyId(long id){
        return documentRepository.findByDocumentId(id);
    }

    public String deleteDocumentById(long id){
        documentRepository.deleteById(id);
        return "success";
    }
    public ArrayList<Document> fullTextSearch(String keyword){
        return documentRepository.fullTextSearch(keyword);
    }
}
