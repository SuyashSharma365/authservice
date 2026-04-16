package com.suyash.authservice.repository;

import com.suyash.authservice.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken , Integer> {


    Optional<RefreshToken> findByToken(String token);
}
