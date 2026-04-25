package com.suyash.authservice.repository;

import com.suyash.authservice.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<UserEntity , String> {

    Optional<UserEntity> findByUsername(String username);

}
