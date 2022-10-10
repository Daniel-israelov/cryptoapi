package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findByIdentityNumber(Long identityNumber);

    Optional<UserEntity> findByFirstNameAndLastName(String firstName, String lastName);

    Boolean existsByIdentityNumber(Long identityNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM user_entity WHERE is_male=:gender")
    List<UserEntity> findAllByIsMale(Boolean gender);

    @Query(nativeQuery = true, value = "SELECT * FROM user_entity WHERE age BETWEEN :from AND :to")
    List<UserEntity> findAllByAgeRange(Integer from, Integer to);
}
