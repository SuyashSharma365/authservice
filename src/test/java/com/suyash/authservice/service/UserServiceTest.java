package com.suyash.authservice.service;

import com.suyash.authservice.entity.UserEntity;
import com.suyash.authservice.model.UserDetailsDto;
import com.suyash.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserDetailsServiceIml userDetailsServiceIml;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    public void shouldCreateUserSuccessfully(){
        UserDetailsDto user = new UserDetailsDto();
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("test@mail.com");

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.empty());


        when(passwordEncoder.encode("password"))
                .thenReturn("encoded123");

        Boolean result  = userDetailsServiceIml.signUpUser(user);
        assertTrue(result);

        verify(passwordEncoder, times(1)).encode("password");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void shouldReturnFalse_whenUserAlreadyExists() {

        UserDetailsDto user = new UserDetailsDto();
        user.setUsername("user");

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(new UserEntity()));

        Boolean result = userDetailsServiceIml.signUpUser(user);

        assertFalse(result);

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldReturnUserDetails_whenUserExists() {
        UserEntity user = new UserEntity();
        user.setUsername("suyash");

        when(userRepository.findByUsername("suyash"))
                .thenReturn(Optional.of(user));

        var result = userDetailsServiceIml.loadUserByUsername("suyash");

        assertNotNull(result);
        assertEquals("suyash", result.getUsername());
    }

    @Test
    void shouldThrowException_whenUserNotFound(){

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class , ()->{
            userDetailsServiceIml.loadUserByUsername("unknown");
        });
    }
}
