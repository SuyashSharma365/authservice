package com.suyash.authservice.controller;

import com.suyash.authservice.entity.RefreshToken;
import com.suyash.authservice.model.UserDetailsDto;
import com.suyash.authservice.response.JwtResponseDTO;
import com.suyash.authservice.service.JwtService;
import com.suyash.authservice.service.RefreshTokenService;
import com.suyash.authservice.service.UserDetailsServiceIml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Exception in User Service");
        }
    }
}
