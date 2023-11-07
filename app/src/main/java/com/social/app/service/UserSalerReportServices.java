package com.social.app.service;

import com.social.app.model.Bill;
import com.social.app.model.Document;
import com.social.app.model.User;
import com.social.app.model.UserSalerReport;
import com.social.app.repository.BillRepository;
import com.social.app.repository.DocumentRepository;
import com.social.app.repository.GroupRepository;
import com.social.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserSalerReportServices {
    @Autowired
    BillRepository billRepository;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    UserService userService;
    @Autowired
    BillService billService;
    @Autowired
    GroupRepository groupRepository;

    public ArrayList<UserSalerReport> saleReport(int userId){
        //tim ra cac doc cua seller
        ArrayList<Document> userDoc = documentRepository.findAllByAuthor(userService.loadUserById(userId));

        ArrayList<Bill> billfind = new ArrayList<>();
        ArrayList<UserSalerReport> saleReport = new ArrayList<>();
        double revenue =0;

        for(Document d: userDoc){
            UserSalerReport userSalerReport = new UserSalerReport();
            billfind = billRepository.findByDocumentDocumentId(d.getDocumentId());
            userSalerReport.setBillReport(billService.ArrayListPostDTO(billfind));
            for(Bill b: billfind){
                revenue =revenue + b.getDocument().getCost();
            }
            userSalerReport.setDocumentName(d.getDocumentName());
            userSalerReport.setTime(d.getTime());
            userSalerReport.setDocumentId(d.getDocumentId());
            userSalerReport.setRevenue(revenue);
            revenue =0;
            userSalerReport.setGroupName(d.getGroup().getGroupName());
            saleReport.add(userSalerReport);
        }
        return saleReport;
    };
}