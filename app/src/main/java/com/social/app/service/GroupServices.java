package com.social.app.service;

import com.social.app.model.Groups;
import com.social.app.model.User;
import com.social.app.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GroupServices {
    @Autowired
    GroupRepository groupRepository;
    public Groups loadGroupById(long gid) throws RuntimeException {
        Groups groups = groupRepository.findByGroupId(gid);
        if(groups!=null)
            return groups;
        else throw new RuntimeException("Not valid group");
    }
}
