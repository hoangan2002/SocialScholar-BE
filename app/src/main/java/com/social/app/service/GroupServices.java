package com.social.app.service;

import com.social.app.dto.CategoryDTO;
import com.social.app.model.Groups;
import com.social.app.model.JoinManagement;
import com.social.app.model.Post;
import com.social.app.model.User;
import com.social.app.dto.GroupDTO;
import com.social.app.dto.UserDTO;
import com.social.app.model.*;
import com.social.app.repository.CategoryRepository;
import com.social.app.repository.GroupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

import org.modelmapper.ModelMapper;
@Service
public class GroupServices {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserService userService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CategoryRepository categoryRepository;

    public Groups loadGroupById(long gid) {
        return groupRepository.findByGroupId(gid);
    }

    public Groups loadGroupByName(String groupName)  {
        return groupRepository.findByGroupName(groupName);
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
        if(groups == null) return false;
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
    public ArrayList<Groups> findAllByCategoryCategoryName(String categoryName){
        return groupRepository.findAllByCategoryCategoryNameIgnoreCase(categoryName);
    }
    public ArrayList<Groups> findAllByTag(String tag){
        return groupRepository.findAllByTagsContainsIgnoreCase(tag);
    }
    public ArrayList<Groups> getRandomGroups(ArrayList<Groups> groups, int n) {
        ArrayList<Groups> result = new ArrayList<>();
        Random random = new Random();


        for (int i = 0; i < n && !groups.isEmpty(); i++) {
            int randomIndex = random.nextInt(groups.size());
            result.add(groups.get(randomIndex));
            groups.remove(randomIndex);
        }

        return result;
    }

    public ArrayList<Groups> findByCategory(Category category){return groupRepository.findByCategory(category);}
<<<<<<< HEAD
    public ArrayList<Groups> fullTextSearch(String keyword){
        return groupRepository.fullTextSearch(keyword);
=======

    public HashMap<CategoryDTO, Integer> getGroupCount(){
        // Get all categories
        List<Category> categories = categoryRepository.findAll();
        // Initialize result hashmap
        HashMap<CategoryDTO, Integer> result = new HashMap<>();
        // For each category, count number of groups of that category then put in result hashmap
        for (Category category: categories) {
            result.put(modelMapper.map(category, CategoryDTO.class), groupRepository.countByCategory(category));
        }
        return result;
>>>>>>> 252ed28d3f7acb0b6916ad9117fea27d12a3c820
    }
}