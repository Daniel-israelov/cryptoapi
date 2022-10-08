package com.example.cryptoapi.repositories;

import com.example.cryptoapi.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByIdentityNumber(Long identityNumber);
}
