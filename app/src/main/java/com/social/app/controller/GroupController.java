package com.social.app.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.social.app.dto.GroupDTO;
import com.social.app.dto.UserDTO;
import com.social.app.dto.Views;
import com.social.app.entity.ResponseObject;
import com.social.app.model.Category;
import com.social.app.model.Groups;

import com.social.app.model.JoinManagement;
import com.social.app.model.User;
import com.social.app.request.GroupRequest;
import com.social.app.service.*;

import com.social.app.service.GroupServices;
import com.social.app.service.ImageStorageService;
import com.social.app.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
public class GroupController {
    @Autowired
    GroupServices groupServices;
    @Autowired
    UserService userService;
    @Autowired
    ImageStorageService imageStorageService;
    @Autowired
    JoinService joinService;
    private final String FOLDER_PATH="D:\\New folder\\upload\\";



    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createGroup(@RequestBody GroupRequest group, @RequestParam(value = "fileGAvatar", required = false) MultipartFile fileGAvatar, @RequestParam(value = "fileGCover", required = false) MultipartFile fileGCover){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            System.out.println(username);
           User user =  userService.findUser(username).orElse(null);
           Groups newGroup = new Groups();
//them ngay tạo
            if(user.getActivityPoint() >= 2000){
                //set ava group

//                if (fileGAvatar!=null) {
//
//                    String fileName = imageStorageService.storeFile(fileGAvatar);
//
//                    group.setImageURLGAvatar(FOLDER_PATH + fileName);
//
//                }

                //set ảnh bìa group
//                if (fileGCover!=null) {
//                    String fileName = imageStorageService.storeFile(fileGCover);
//                    group.setImageUrlGCover(FOLDER_PATH + fileName);
//                }
                newGroup.setHosts(user);
                newGroup.setGroupName(group.getGroupName());
                newGroup.setDescription(group.getDescription());
                userService.setRoleHost(user);
//                Category category = new Category(9,"Xã Hội");
//                newGroup.setCategory(category);
                newGroup.setTags(group.getTags());
                //Thời gian tạo

                newGroup.setTimeCreate(group.getTimeCreate());
                groupServices.createGroup(newGroup);
                joinService.saveJoin(new JoinManagement(newGroup,user,Calendar.getInstance().getTime()));

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Create Group Success", "OK", null));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Create Group Fail", "ERROR",null));
        } catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new ResponseObject("Create Group THROW", "ERROR",null ));}

    }
    @PutMapping("/update/{groupId}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<ResponseObject> updateGroup(@PathVariable Long groupId,@RequestBody Groups updatedGroup,@RequestParam(value = "fileGAvatar", required = false) MultipartFile fileGAvatar, @RequestParam(value = "fileGCover", required = false) MultipartFile fileGCover) {
        try {       if(groupServices.isGroupHost(groupId)) {
            Groups groups= groupServices.loadGroupById(groupId);
            MultipartFile[] file = {fileGAvatar , fileGCover};
            if(groups != null){
                if (updatedGroup.getDescription() !=null ){
                    groups.setDescription(updatedGroup.getDescription());
                }
                if (updatedGroup.getGroupName() !=null){
                    groups.setGroupName(updatedGroup.getGroupName());
                }
                if (updatedGroup.getCategory() != null){
                    groups.setCategory(updatedGroup.getCategory() );
                }
                if (updatedGroup.getHosts() != null){
                    groups.setHosts(updatedGroup.getHosts());
                }
                if (fileGAvatar!=null) {
                    String fileName = imageStorageService.storeFile(fileGAvatar);
                    groups.setImageURLGAvatar(FOLDER_PATH + fileName);
                }
                if (fileGCover!=null) {
                    String fileName = imageStorageService.storeFile(fileGCover);
                    groups.setImageUrlGCover(FOLDER_PATH + fileName);
                }
                return  ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Update Group Success", "OK",groupServices.updateGroup(groups)));
            }}
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Update Group Fail", "ERROR",null));
        }  catch (RuntimeException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( new ResponseObject("Create Group THROW", "ERROR",null ));}}
    @PostMapping("/delete/{groupId}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public ResponseEntity<ResponseObject> deleteGroup(@PathVariable Long groupId) {
        if(groupServices.isGroupHost(groupId)) {
            Groups group = groupServices.loadGroupById(groupId);
            if (group != null) {
                groupServices.deleteGroup(groupId);
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Delete Group Success", "OK", null));
            }
        }return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Delete Group Fail", "ERROR", null));
    }
    @GetMapping("/{groupId}")
    public  GroupDTO readGroup(@PathVariable Long groupId){
//        if(groupServices.isGroupHost(groupId)){
//
//        }return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Read Group Fail", "ERROR",null));
        System.out.println(groupId);
        try {
            Groups group  = groupServices.loadGroupById(groupId);

            if(group == null) ;
            System.out.println(groupId);
            System.out.println(group);
            if (group == null) {
                throw new Exception("Group not found"); // Ném ngoại lệ nếu người dùng không tồn tại
            }

            System.out.println("ssssssssssss");
            return groupServices.MapGroupDTO(group);
        } catch (Exception e) {
            // Xử lý ngoại lệ UserNotFoundException ở đây
            return null;
        }
    }

    @PostMapping("/find-group")
    public ArrayList<Groups> findPost(@RequestBody String findContent){
        ArrayList<Groups> allGroup = groupServices.retriveGroupFromDB();
        ArrayList<Groups> findResult = new ArrayList<>();
        if(findContent!= null && findContent != "\s") {
            for (Groups p : allGroup) {
                if (p.getGroupName() != null && p.getGroupName().toLowerCase().contains(findContent.toLowerCase().trim())) {
                    findResult.add(p);
                }
            }
        }else {
            return null;
        }
        return findResult;
    }
    @PostMapping("/join-group/{groupId}")
    public  ResponseEntity<ResponseObject> joinGroup(@PathVariable Long groupId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(userService.isGroupMember(groupId) || groupServices.isGroupHost(groupId)){
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("User Joined Group", "ERROR",null));
        }

        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Join Success", "OK",joinService.saveJoin(new JoinManagement(groupServices.loadGroupById(groupId), userService.findUserByUsername(username), Calendar.getInstance().getTime()))));
    }
    @PostMapping("/exit-group/{groupId}")
    public  ResponseEntity<ResponseObject> exitGroup(@PathVariable Long groupId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(!userService.isGroupMember(groupId) || groupServices.isGroupHost(groupId)){
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("User not in Group/ User is Host", "ERROR",null));
        }
        joinService.deleteJoin(groupServices.loadGroupById(groupId),userService.findUserByUsername(username));
        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Exit Success", "OK",null));
    }
    @PostMapping("/kick-user/{groupId}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    public  ResponseEntity<ResponseObject> kickUser(@PathVariable Long groupId,@RequestParam String userName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameHost = authentication.getName();
        if(!groupServices.isGroupHost(groupId)){
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("User is not Host of Group", "ERROR",null));
        }
        //cannot self-provoke
        if(groupServices.isGroupHost(groupId, userName)){
            return   ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("Can't kick Host of Group", "ERROR",null));
        }
        joinService.deleteJoin(groupServices.loadGroupById(groupId),userService.findUserByUsername(userName));
        return   ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("Kick out Success", "OK", null));


    }
// đề xuất group
    @PostMapping ("/suggest")
    @JsonView(Views.GroupsViewSuggest.class)
    public ArrayList<GroupDTO> suggestGroups(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ArrayList<Groups> groups =  groupServices.findAll();
        ArrayList<GroupDTO> groupsSuggest = new ArrayList<>();
         //Gợi ý
        for (Groups group :groups )  {
            GroupDTO groupDTO= groupServices.MapGroupDTO(group);
            groupDTO.setIsJoin(false);
            if(!userService.isGroupMember(username,group.getGroupId())){
               groupsSuggest.add(groupDTO);
            }
        }

        return  groupsSuggest;
    }
    // tìm group bằng tên
    @GetMapping("/find-group-login/{groupName}")
    @JsonView(Views.GroupsViewSuggest.class)
    public ArrayList<GroupDTO> findGroups(@PathVariable String groupName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ArrayList<Groups> groups =  groupServices.findAll();
        ArrayList<GroupDTO> groupsSuggest = new ArrayList<>();
        //Search group
        if(groupName!=null){
            ArrayList<GroupDTO> joinedGroups  = new ArrayList<>();
            ArrayList<GroupDTO> otherGroups  = new ArrayList<>();
            for (Groups group :groups )  {
                GroupDTO groupDTO= groupServices.MapGroupDTO(group);
                if(userService.isGroupMember(username,group.getGroupId()) && group.getGroupName().toLowerCase().contains(groupName.toLowerCase().trim())){

                    groupDTO.setIsJoin(true);
                    joinedGroups.add(groupDTO);
                } else if( group.getGroupName().contains(groupName)){

                    groupDTO.setIsJoin(false);
                    otherGroups.add(groupDTO);
                }
            }
            groupsSuggest.addAll(joinedGroups);
            groupsSuggest.addAll(otherGroups);
            return groupsSuggest;
        } else return null;


    }
    // tìm group bằng category
    @PostMapping("/find-group-by-category/{categorys}")
    @JsonView(Views.GroupsViewSuggest.class)
    public ArrayList<GroupDTO> findGroupsByCategory(@PathVariable String categorys) {
        ArrayList<Groups> listGroups = new ArrayList<>();

        String[] category = categorys.split(",");
        for(int i =0; i<category.length;i++){

            category[i] = category[i].trim();

            listGroups.addAll(groupServices.findAllByCategoryCategoryName(category[i]));
        }

        Comparator<Groups> groupComparator = (group2, group1) ->{ return Integer.compare(group1.getJoins().size(),group2.getJoins().size());};

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if(authentication != null){
            String username = authentication.getName();
            User user= userService.loadUserByUserName(username);
            ArrayList<Groups> userGroups  = new ArrayList<>();
            for (JoinManagement join: user.getJoins()) { userGroups.add(join.getGroup()); }
            listGroups.removeAll(userGroups);
        }

        ArrayList<Groups> randomGroups = groupServices.getRandomGroups(listGroups, 5);
        Collections.sort(randomGroups,groupComparator);
        return  groupServices.groupsResponses(randomGroups);
    }
    // tìm group bằng hasstag
    @PostMapping("/find-group-by-hashtag/{hashtags}")
    @JsonView(Views.GroupsViewHashTag.class)
    public  ArrayList<GroupDTO> findGroupsByHashTag(@PathVariable String hashtags){
        ArrayList<Groups> listGroups = groupServices.findAll();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Lấy ra các group user chưa join
        if(authentication != null){
            String username = authentication.getName();
            User user= userService.loadUserByUserName(username);
            ArrayList<Groups> userGroups  = new ArrayList<>();
            for (JoinManagement join: user.getJoins()) { userGroups.add(join.getGroup()); }
            listGroups.removeAll(userGroups);
        }
        //Lấy ra các group có matching
        Map<Groups, Integer> groupMatchingCount = new HashMap<>();

        for (Groups groups:listGroups) {
            if(groups.getTags()!=null){



            String[] groupHashtagArray = groups.getTags().split(",");

            String[] hashtag = hashtags.split(",");

            Set<String> hashtagset = new LinkedHashSet<>(Arrays.asList(hashtag));
            int matchingCount = 0;
            for (String inputHashtag : hashtagset) {
                for (String groupHashtag : groupHashtagArray) {
                    if (inputHashtag.trim().toLowerCase().equals(groupHashtag.trim().toLowerCase())) {
                        matchingCount++;
                    }
                }
            }
            if(matchingCount!= 0) {
                groupMatchingCount.put(groups, matchingCount);
            }
            }
        }
        //sắp xếp theo độ matching
        Map<Groups, Integer> sortedMap = groupMatchingCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
       ArrayList<Groups> result = new ArrayList<>();
        Iterator<Groups> iterator = sortedMap.keySet().iterator();
        int count =0;
        while (iterator.hasNext()) {
              if(count<5){
                  result.add(iterator.next());
                  count++;
              } else break;
        }
           return groupServices.groupsResponses(result);
    }
    //lấy ra tất cả group mà user tham gia
    @GetMapping("/get-all-group/{userId}")
    public ArrayList<GroupDTO> getAllGroupOfUser(@PathVariable int userId){
        User user= userService.loadUserById(userId);
        ArrayList<Groups> groups  = new ArrayList<>();
        for (JoinManagement join: user.getJoins()
        ) {
            groups.add(join.getGroup());
        }
        return  groupServices.groupsResponses(groups);
    }
    //Lấy tất cả group
    @GetMapping("/getAllGroup")
    public ArrayList<GroupDTO> getAllGroup(){
        ArrayList<Groups> groups  = groupServices.retriveGroupFromDB();
        return  groupServices.groupsResponses(groups);
    }

    //Lấy tất cả user của 1 group
    @GetMapping("/getalluser/{groupId}")
    @PreAuthorize("hasRole('ROLE_HOST')")
    @JsonView(Views.UserView1.class)
    public ArrayList<UserDTO> getAllUserByGroup(@PathVariable long groupId){
        Groups group = groupServices.loadGroupById(groupId);
        ArrayList<User> users = new ArrayList<>();
        for (JoinManagement join: group.getJoins()
        ) {
            users.add(join.getUser());
        }
        return userService.userResponses(users);
    }


    @GetMapping("/getNumberOfMembers/{groupid}")
    public ResponseEntity<ResponseObject> getNumberOfMembers (@PathVariable long groupid) {
        if (groupServices.loadGroupById(groupid) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "Group not exist", "")
            );
        else {
            Groups groups = groupServices.loadGroupById(groupid);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Success", "Done!", groups.getJoins().size())
            );
        }
    }

    // thuật toán search: hashtags=>groups
    @GetMapping("/search-by-hashtag/{hashtags}")
    @JsonView(Views.GroupsView.class)
    public  ArrayList<GroupDTO> searchGroupsByHashTag(@PathVariable String hashtags){
        ArrayList<Groups> listGroups = groupServices.findAll();
        //Lấy ra các group có matching

        Map<Groups, Integer> groupMatchingCount = new HashMap<>();

        for (Groups groups:listGroups) {
            if(groups.getTags()!=null){
                String[] groupHashtagArray = groups.getTags().split(",");
                String[] hashtag = hashtags.split(",");
                Set<String> hashtagset = new LinkedHashSet<>(Arrays.asList(hashtag));
                int matchingCount = 0;
                for (String inputHashtag : hashtagset) {
                    for (String groupHashtag : groupHashtagArray) {
                        if (inputHashtag.trim().toLowerCase().equals(groupHashtag.trim().toLowerCase())) {
                            matchingCount++;
                        }
                    }
                }
                if(matchingCount!= 0) {
                    groupMatchingCount.put(groups, matchingCount);
                }
            }
        }

        //sắp xếp theo độ matching
        Map<Groups, Integer> sortedMap = groupMatchingCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        ArrayList<Groups> result = new ArrayList<>();

        Iterator<Groups> iterator = sortedMap.keySet().iterator();


        while (iterator.hasNext()) {

                result.add(iterator.next());

        }
        return groupServices.groupsResponses(result);
    }
}
