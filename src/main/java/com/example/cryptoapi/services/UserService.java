package com.example.cryptoapi.services;

import com.example.cryptoapi.assemblers.UserDtoAssembler;
import com.example.cryptoapi.dtos.UserDto;
import com.example.cryptoapi.entities.UserEntity;
import com.example.cryptoapi.exceptions.UserAlreadyExistsException;
import com.example.cryptoapi.exceptions.UserNotFoundException;
import com.example.cryptoapi.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
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

        Optional<UserEntity> user = userRepository.findByIdentityNumber(identityNumber);
        if (user.isEmpty()) {
            throw new UserNotFoundException(identityNumber);
        }
        log.info("User with id = " + identityNumber + " returned in service = " + user);
        UserDto userDto = new UserDto(user.get());
        return userDtoAssembler.toModel(userDto);
    }

    public EntityModel<UserDto> findByFullName(String firstName, String lastName) {
        Optional<UserEntity> user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        if (user.isEmpty()) {
            throw new UserNotFoundException(firstName, lastName);
        }
        return userDtoAssembler.toModel(new UserDto(user.get()));
    }

    public EntityModel<UserDto> createUser(@NotNull UserEntity userEntity) {
        if (userRepository.existsByIdentityNumber(userEntity.getIdentityNumber())) {
            throw new UserAlreadyExistsException(userEntity.getIdentityNumber());
        }
        log.info("Saving new user to database...");
        userRepository.save(userEntity);
        return userDtoAssembler.toModel(new UserDto(userEntity));
    }

    public CollectionModel<EntityModel<UserDto>> findAllByGender(String gender) {
        if (!gender.equals("male") && !gender.equals("female")) {
            String msg = "Invalid gender input: '" + gender + "'";
            log.info(msg);
            return null;
        }
        List<UserEntity> allByGender = userRepository.findAllByIsMale(gender.equals("male"));

        return userDtoAssembler.toCollectionModel(allByGender
                .stream()
                .map(UserDto::new)
                .toList());
    }

    public void deleteByIdentityNumber(Long identityNumber) {
        UserEntity user = userRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new UserNotFoundException(identityNumber));

        userRepository.deleteById(user.getId());
        log.info("deleted");
    }

/*    public EntityModel<UserDto> updateUser(Long identityNumber, UserEntity userEntity) {
        UserEntity user = userRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new UserNotFoundException(identityNumber));

        Field[] fields = user.getClass().getDeclaredFields();

        for(Field f:fields){
            try {
                Object x = f.get(userEntity);
                if (x != null) {
                    f.set(user, x);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return userDtoAssembler.toModel(new UserDto(user));
    }*/
}
