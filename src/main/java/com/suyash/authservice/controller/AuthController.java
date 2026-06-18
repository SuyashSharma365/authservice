package com.suyash.authservice.controller;

import com.suyash.authservice.entity.RefreshToken;
import com.suyash.authservice.entity.UserEntity;
import com.suyash.authservice.model.UserDetailsDto;
import com.suyash.authservice.response.JwtResponseDTO;
import com.suyash.authservice.service.JwtService;
import com.suyash.authservice.service.RefreshTokenService;
import com.suyash.authservice.service.UserDetailsServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsServiceIml userDetailsServiceIml;

    @PostMapping("/signup")
    public ResponseEntity<?>signUpi(@RequestBody UserDetailsDto userDetailsDto){
        try{
            Boolean isSignUp = userDetailsServiceIml.signUpUser(userDetailsDto);

            if(Boolean.FALSE.equals(isSignUp)){
                return ResponseEntity.badRequest().body("User already exists");
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetailsDto.getUsername());
            String jwtToken = jwtService.createToken(new HashMap<>() , userDetailsDto.getUsername());
            return ResponseEntity.ok(JwtResponseDTO.builder().accessToken(jwtToken).refreshToken(refreshToken.getToken()).build());
        }catch (Exception ex){
            logger.error("Signup failed", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Exception in User Service");
        }
    }

    @GetMapping("/v1/ping")
    public ResponseEntity<String>ping(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated()){
            Optional<UserEntity> user = userDetailsServiceIml.findUserByUsername(auth.getName());
            if(user.isPresent()){
                return ResponseEntity.ok(user.get().getUserId());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }

}
