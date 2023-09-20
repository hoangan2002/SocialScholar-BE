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

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;


    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userDetail = repository.findByName(username);

        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }

    public User addUser(User request) {
        var user = User.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .roles(String.valueOf(Role.ROLE_USER))
                .build();
        System.out.println(user);
        repository.save(user);
        return user;
    }

    public Boolean isExits(User user)
    {
        return  repository.findByName(user.getName()).isPresent() || repository.findByEmail(user.getEmail()).isPresent();
    }



}
