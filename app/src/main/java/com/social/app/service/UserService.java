package com.social.app.service;
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
    @Transactional
    public User save(User theUser){
        return repository.save(theUser);
    }
}
