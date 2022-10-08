package com.example.cryptoapi.services;

import com.example.cryptoapi.assemblers.UserDtoAssembler;
import com.example.cryptoapi.dtos.UserDto;
import com.example.cryptoapi.entities.User;
import com.example.cryptoapi.exceptions.UserNotFoundException;
import com.example.cryptoapi.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoAssembler userDtoAssembler;

    public UserService(UserRepository userRepository, UserDtoAssembler userDtoAssembler) {
        this.userRepository = userRepository;
        this.userDtoAssembler = userDtoAssembler;
    }

    public CollectionModel<EntityModel<UserDto>> getAllUsers() {

        List<UserDto> dtoUsers = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(UserDto::new).collect(Collectors.toList());
        return userDtoAssembler.toCollectionModel(dtoUsers);
    }

    public EntityModel<UserDto> getUserByIdentityNumber(Long identityNumber) {

        User user = userRepository.findByIdentityNumber(identityNumber);
        log.info("User with id = " + identityNumber + " returned in service = " + user);
        UserDto userDto = new UserDto(user);
        if (user == null) {
            throw new UserNotFoundException(identityNumber);
        }
        return userDtoAssembler.toModel(userDto);
    }
}
