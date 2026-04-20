package com.suyash.authservice.service;

import com.suyash.authservice.entity.UserEntity;
import com.suyash.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserDetailsServiceIml  implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceIml(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> userObject = userRepository.findByUsername(username);

        if(userObject.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        UserEntity user = userObject.get();
        return new CustomUserDetails(user);

    }
}
