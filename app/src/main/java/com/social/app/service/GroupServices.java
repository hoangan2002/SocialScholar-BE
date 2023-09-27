package com.social.app.service;

import com.social.app.model.Groups;
import com.social.app.model.User;
import com.social.app.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
<<<<<<< HEAD
    public Groups createGroup(Groups groups){
       return groupRepository.save(groups);
    }
    public Groups updateGroup(Groups groups){
        return groupRepository.save(groups);
    }
    public void deleteGroup(Long id){
         groupRepository.deleteById(id);
    }
    public boolean isGroupHost(Authentication authentication, Long groupId) {

        User user = (User) authentication.getPrincipal();
       Groups groups= groupRepository.findByGroupId(groupId);
       if(groups.getHosts().equals(user)){
            return true;
       }
        return false;


    }
=======


>>>>>>> 7c8b39897e45eda3f00291baec564136743e1e85
}
