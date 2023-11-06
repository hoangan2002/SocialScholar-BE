package com.social.app.service;

import com.social.app.dto.BillDTO;
import com.social.app.model.Bill;
import com.social.app.model.Document;
import com.social.app.model.User;
import com.social.app.repository.BillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BillService {
    @Autowired
    BillRepository billRepository;

    @Autowired
    ModelMapper modelMapper;
    public BillDTO MapBillDTO(Bill bill){
        BillDTO billDTO = modelMapper.map(bill,BillDTO.class);
        return billDTO;
    }

    public ArrayList<BillDTO> ArrayListPostDTO(ArrayList<Bill> bill){
        ArrayList<BillDTO> billDTOS = new ArrayList<>();
        for(Bill p: bill){
            BillDTO pDTO = MapBillDTO(p);
            billDTOS.add(pDTO);
        }
        return billDTOS;
    }
    public Bill findByDocumentAndUser(Document doc, User user){
        return billRepository.findByDocumentAndUser(doc,user);
    }
    public ArrayList<Bill> findByUser(User user){
        return billRepository.findByUser(user);
    };
    public ArrayList<Bill> allBuill(){return billRepository.findAll();}
}
