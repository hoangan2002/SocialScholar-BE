package com.social.app.service;

import com.social.app.dto.GroupDTO;
import com.social.app.dto.UserDTO;
import com.social.app.model.*;
import com.social.app.repository.GroupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class GroupServices {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserService userService;
    @Autowired
    ModelMapper modelMapper;

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
        System.out.println(groups.getHosts().getUserName());
        System.out.println(user.getUsername());
        if(groups.getHosts().getUserName().equals(user.getUsername())){
            System.out.println("in ra true");
            return true;
        }
        System.out.println("in ra false");
        return false;


    }
    public boolean isGroupHost( Long groupId, String userName) {

        Groups groups= groupRepository.findByGroupId(groupId);
        if(groups.getHosts().getUserName().equals(userName)){
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
    public ArrayList<GroupDTO> groupsResponses(ArrayList<Groups> groups){
        ArrayList<GroupDTO> groupDTOS = new ArrayList<>();
        for (Groups group : groups) {
            groupDTOS.add(MapGroupDTO(group));
        }
        return groupDTOS;
    }
    public GroupDTO MapGroupDTO(Groups groups){
        GroupDTO groupDTO = modelMapper.map(groups,GroupDTO.class);
        return groupDTO;
    }
    public ArrayList<Groups> findAll(){
        return  groupRepository.findAll();
    }


}
