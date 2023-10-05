package com.social.app.service;

import com.social.app.model.Bill;
import com.social.app.model.Document;
import com.social.app.model.User;
import com.social.app.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillService {
    @Autowired
    BillRepository billRepository;
    public Bill findByDocumentAndUser(Document doc, User user){
        return billRepository.findByDocumentAndUser(doc,user);
    }
}
