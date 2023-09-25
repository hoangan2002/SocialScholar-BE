package com.social.app.service;
import com.social.app.model.MemberActivity;
import com.social.app.model.Role;
import com.social.app.model.User;
import com.social.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;


    @Autowired
    private PasswordEncoder encoder;

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
                .password(encoder.encode(request.getPassword()))
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
    public Boolean existPhone(String phone){
        Optional<User> result = repository.findByUserName(phone);
        return result.isPresent();
    }

    // Cong diem cho user, goi ham moi khi thuc hien hanh dong
    public void addPoints(User user, Enum<MemberActivity> activity){
        int activityPoint = user.getActivityPoint();
        if(activity.equals(MemberActivity.COMMENT_VOTED))
            user.setActivityPoint(activityPoint+2);
        else if (activity.equals(MemberActivity.DONATE)||activity.equals(MemberActivity.VOTE)||activity.equals(MemberActivity.BUY_DOCUMENT)||activity.equals(MemberActivity.POST_VOTED))
            user.setActivityPoint(activityPoint+5);
        else if (activity.equals(MemberActivity.REPORT))
            user.setActivityPoint(activityPoint+10);
        else if (activity.equals(MemberActivity.POST))
            user.setActivityPoint(activityPoint+20);
        else throw new RuntimeException("Invalid activity");
    }
}
