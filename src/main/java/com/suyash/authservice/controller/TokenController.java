package com.suyash.authservice.controller;

import com.suyash.authservice.entity.RefreshToken;
import com.suyash.authservice.entity.UserEntity;
import com.suyash.authservice.request.AuthRequestDTO;
import com.suyash.authservice.request.RefreshTokenRequestDTO;
import com.suyash.authservice.response.JwtResponseDTO;
import com.suyash.authservice.service.JwtService;
import com.suyash.authservice.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class TokenController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername() , authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            JwtResponseDTO response = JwtResponseDTO
                    .builder().accessToken(jwtService.createToken(new HashMap<>() , authRequestDTO.getUsername()))
                    .refreshToken(refreshToken.getToken())
                    .build();
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.internalServerError().body("Exception in User Service");
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.getToken());

        if(optionalRefreshToken.isEmpty()){
            throw new RuntimeException("Invalid Refresh Token");
        }
        RefreshToken oldRefreshToken = optionalRefreshToken.get();
        refreshTokenService.verifyExpiration(oldRefreshToken);
        UserEntity user = oldRefreshToken.getUserEntity();
        String accessToken = jwtService.createToken(new HashMap<>() , user.getUsername());
        JwtResponseDTO responseDTO =  JwtResponseDTO.builder().accessToken(accessToken).refreshToken(refreshTokenRequestDTO.getToken()).build();
        return ResponseEntity.ok(responseDTO);
    }
}
