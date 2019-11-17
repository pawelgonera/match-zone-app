package pl.poul12.matchzone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.poul12.matchzone.model.User;
import pl.poul12.matchzone.security.UserPrinciple;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserDetailsServiceImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User userDetails = userServiceImpl.getUserByUsername(username);
        System.out.println("userdetails" + userDetails.getUsername() + " : " + userDetails.getPassword());
        return UserPrinciple.build(userDetails);
    }
}
