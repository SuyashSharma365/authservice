package com.suyash.authservice.service;
import com.suyash.authservice.entity.UserEntity;
import com.suyash.authservice.model.UserDetailsDto;
import com.suyash.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserDetailsServiceIml  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceIml(UserRepository userRepository , PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public Optional<UserEntity> userExists(UserDetailsDto userDetailsDto){
        Optional<UserEntity> userObj = userRepository.findByUsername(userDetailsDto.getUsername());
        return userObj;
    }

    public Boolean signUpUser(UserDetailsDto userDetailsDto){
        if(userExists(userDetailsDto).isPresent()){
            return false;
        }
        userDetailsDto.setPassword(passwordEncoder.encode(userDetailsDto.getPassword()));
        String userId = UUID.randomUUID().toString();
        userRepository.save(new UserEntity(userId,
                userDetailsDto.getUsername(),
                userDetailsDto.getPassword(),
                userDetailsDto.getEmail(),
                new HashSet<>()));
        return true;
    }

}
