package com.modugarden.domain.user.service;

import com.modugarden.domain.user.entity.User;
import com.modugarden.domain.user.repository.UserRepository;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@Data
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        UserService userDetails = null;
        if (user != null) {
            userDetails = new UserService();
//            userDetails.setUser(user);
        }
        return null;
    }
}
