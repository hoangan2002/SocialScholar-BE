package com.social.app.service;

import com.social.app.model.Groups;
import com.social.app.model.JoinManagement;
import com.social.app.model.User;
import com.social.app.repository.JoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JoinService {
    @Autowired
    JoinRepository joinRepository;
    public JoinManagement saveJoin(JoinManagement joinManagement){ return joinRepository.save(joinManagement);}
    @Transactional
    public void deleteJoin(Groups groups, User user){  joinRepository.deleteByGroupAndUser(groups, user);}
}
