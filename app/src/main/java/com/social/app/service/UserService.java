package com.social.app.service;
import com.social.app.config.ModelMapperConfig;
import com.social.app.config.SecurityConfig;
import com.social.app.dto.GroupDTO;
import com.social.app.dto.UserDTO;
import com.social.app.model.*;
import com.social.app.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private CommentReportRepository commentReportRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostReportRepository postReportRepository;
    @Autowired
    private JoinRepository joinRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TokenPaymentRepository tokenPaymentRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    ModelMapper modelMapper;

    public UserDTO MapUserDTO(User user){
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userDetail = repository.findByUserName(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public User addUser(User request) {
        var user = User.builder()
                .userName(request.getUserName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword().trim()))
                .role(String.valueOf(Role.ROLE_USER))
                .build();
        System.out.println(user);
        repository.save(user);
        return user;
    }

    public Boolean isExits(User user)
    {
        return  repository.findByUserName(user.getUserName()).isPresent() || repository.findByEmail(user.getEmail()).isPresent();
    }


    public User findById(int theId){
        Optional<User> result = repository.findById(theId);
        User theUser = null;
        if (result.isPresent()){
            theUser = result.get();
        }
        else throw new RuntimeException("Did not find employee id - " + theId);
        return theUser;
    }

    public Optional<User> findUser(String userName)
    {
        return repository.findByUserName(userName);
    }

    public Boolean existUserName(String theName){
        Optional<User> result = repository.findByUserName(theName);
        return result.isPresent();
    }
    public  Optional<User>  findByEmail(String email ){
        return   repository.findByEmail(email);
    }

    public User save(User theUser){
        return repository.save(theUser);
    }
    public User updatePassword(String email, String password){
        User user = repository.findByEmail(email).orElse(null);
        if (user != null) {

            user.setPassword(encoder.encode(password));

            repository.save(user);
            return user;
        } else {
            return null;
        }

    }


    public User loadUserById(int id) throws UsernameNotFoundException {
        Optional<User> result = repository.findById(id);
        User theUser = null;
        if (result.isPresent()) {
            theUser = result.get();
        } else throw new RuntimeException("Did not find employee id - " + id);
        return theUser;
    }

    public User loadUserByUserName(String userName){
        User user = repository.findByUserName(userName).orElseThrow(()-> new RuntimeException("User does not exist!"));
        return user;
    }
    public Boolean existPhone(String phone){
        Optional<User> result = repository.findByPhone(phone);
        return result.isPresent();
    }


    public void setRoleHost(User user) {
        String role = user.getRole();
        if(role.contains("ROLE_HOST")){
            return;
        }
        user.setRole(role +  ",ROLE_HOST");
        repository.save(user);
    }

    // Cong diem cho user, goi ham moi khi thuc hien hanh dong, NHAP vao User
    public User addPoints(User user, Enum<MemberActivity> activity){
        int activityPoint = user.getActivityPoint();
        String originLevel = user.getLevel();
        // set point
        if(activity.equals(MemberActivity.COMMENT_VOTED))
            user.setActivityPoint(activityPoint+2);
        else if (activity.equals(MemberActivity.DONATE)||activity.equals(MemberActivity.VOTE)||activity.equals(MemberActivity.BUY_DOCUMENT)||activity.equals(MemberActivity.POST_VOTED))
            user.setActivityPoint(activityPoint+5);
        else if (activity.equals(MemberActivity.REPORT))
            user.setActivityPoint(activityPoint+10);
        else if (activity.equals(MemberActivity.POST))
            user.setActivityPoint(activityPoint+20);
        else throw new RuntimeException("Invalid activity");

        // activity point sau khi set
        activityPoint = user.getActivityPoint();

        // setLevel
        if (activityPoint<500)
            user.setLevel(String.valueOf(MemberType.BEGINNER));
        else if (activityPoint<2000)
            user.setLevel(String.valueOf(MemberType.ACTIVE));
        else
            user.setLevel(String.valueOf(MemberType.EXPERT));

        // so sanh level trc sau va hien thbao
        String updateLevel = user.getLevel();
        if (!originLevel.equals(updateLevel)){
            System.out.println("CONGRATULATION, YOUR LEVEL IS UPGRADED TO: "+ updateLevel);
        }

        // luu vao database
        return repository.findById(user.getUserId())
                .map(userDb -> {
                    userDb.setActivityPoint(user.getActivityPoint());
                    userDb.setLevel(updateLevel);
                    return repository.save(userDb);
                } ).orElseThrow(()-> new RuntimeException("Fail!"));
    }

    // Cong diem cho user, goi ham moi khi thuc hien hanh dong, NHAP vao ID cua user
    public User addPoints(int userId, Enum<MemberActivity> activity){
        User user = findById(userId);
        if (user==null) throw new RuntimeException("Did not find employee id - " + userId);
        int activityPoint = user.getActivityPoint();
        String originLevel = user.getLevel();
        // set point
        if(activity.equals(MemberActivity.COMMENT_VOTED))
            user.setActivityPoint(activityPoint+2);
        else if (activity.equals(MemberActivity.DONATE)||activity.equals(MemberActivity.VOTE)||activity.equals(MemberActivity.BUY_DOCUMENT)||activity.equals(MemberActivity.POST_VOTED))
            user.setActivityPoint(activityPoint+5);
        else if (activity.equals(MemberActivity.REPORT))
            user.setActivityPoint(activityPoint+10);
        else if (activity.equals(MemberActivity.POST))
            user.setActivityPoint(activityPoint+20);
        else throw new RuntimeException("Invalid activity");

        // activity point sau khi set
        activityPoint = user.getActivityPoint();

        // setLevel
        if (activityPoint<500)
            user.setLevel(String.valueOf(MemberType.BEGINNER));
        else if (activityPoint<2000)
            user.setLevel(String.valueOf(MemberType.ACTIVE));
        else
            user.setLevel(String.valueOf(MemberType.EXPERT));

        // so sanh level trc sau va hien thbao
        String updateLevel = user.getLevel();
        if (!originLevel.equals(updateLevel)){
            System.out.println("CONGRATULATION, YOUR LEVEL IS UPGRADED TO: "+ updateLevel);
        }
        // save
        return repository.save(user);
    }

    public boolean isGroupMember(long groupId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user =  findUserByUsername(username);
        // Get list joinmanagement by user
        ArrayList<JoinManagement> joins = joinRepository.findByUser(user);
        for (JoinManagement join:joins) {
            // Check if user joined in group, return true, else return false
            if (join.getGroup().getGroupId() == groupId) return true;
        }
        return  false;
    }
    public boolean isGroupMember(String userName, long groupId){
        // Get user by userId
        User user = loadUserByUserName(userName);
       // User user = (User) loadUserByUsername(userName);
        // Get list joinmanagement by user
        ArrayList<JoinManagement> joins = joinRepository.findByUser(user);
        for (JoinManagement join:joins) {
            // Check if user joined in group, return true, else return false
            if (join.getGroup().getGroupId() == groupId) return true;
        }
        return  false;
    }

    public boolean isCommemtCreator(int userId, long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new RuntimeException("Not found comment"));
        if (userId == comment.getUser().getUserId()) return true;
        return false;
    }

    public User findUserByUsername(String username){
        return repository.findUserByUserName(username);
    }

    public ArrayList<User> findAll() {
        return repository.findAll();
    }
    public String encodePass(String pass){
        return  securityConfig.passwordEncoder().encode(pass);

    }

    public ArrayList<UserDTO> userResponses(ArrayList<User> users) {
        ArrayList<UserDTO> userDTOSDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOSDTOS.add(MapUserDTO(user));
        }
        return userDTOSDTOS;
    }
    public User plusPoint(int userId, int points){
        User user = loadUserById(userId);
        if(user==null) return null;
        else {
            user.setActivityPoint(user.getActivityPoint()+points);
            save(user);
        }
        return user;
    }

    @Transactional
    public void deleteUser(String username){
        User user = findUserByUsername(username);
        if (user==null) throw new RuntimeException("Not found user");
        // delete hostId in groups
        ArrayList<Groups> groups = groupRepository.findByHosts(user);
        for (Groups group: groups) {
            group.setHosts(null);
        }
        // final delete in user table
        repository.deleteById(user.getUserId());
    }


    public long countUser() {
        ArrayList<User> users = repository.findAll();
        long count = users.size();
        return count;

    }
    public  User findFirstUser(){
        return  repository.findFirstByOrderByUserId();
    }
}
