package com.social.app.service;

import com.social.app.model.Groups;
import com.social.app.model.JoinManagement;
import com.social.app.model.Post;
import com.social.app.model.User;
import com.social.app.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class GroupServices {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserService userService;

    public Groups loadGroupById(long gid) throws RuntimeException {
        Groups groups = groupRepository.findByGroupId(gid);
        if(groups!=null)
            return groups;
        else throw new RuntimeException("Not valid group");
    }

    public Groups loadGroupByName(String groupName) throws RuntimeException {
        Groups groups = groupRepository.findByGroupName(groupName);
        if(groups!=null)
            return groups;
        else throw new RuntimeException("Not valid group");
    }
    public Groups createGroup(Groups groups){
       return groupRepository.save(groups);
    }
    public Groups updateGroup(Groups groups){
        return groupRepository.save(groups);
    }
    public void deleteGroup(Long id){
         groupRepository.deleteById(id);
    }
    public boolean isGroupHost( Long groupId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails user =userService.loadUserByUsername(authentication.getName());
        Groups groups= groupRepository.findByGroupId(groupId);
        if(groups.getHosts().getUserName().equals(user.getUsername())){
            return true;
        }
        return false;


    }
    public ArrayList<Groups> retriveGroupFromDB(){
        ArrayList<Groups> result = groupRepository.findAll();
        return result;
    }

    public User findUserMember(long userId, long groupId) {
        Groups group = loadGroupById(groupId);
        Optional<User> foundUser = group.getJoins().stream()
                .map(JoinManagement::getUser)
                .filter(user -> user.getUserId() == userId) // Sử dụng phương thức getUser() để lấy thông tin User
                .findFirst();

        return foundUser.orElse(null);
    }


}
