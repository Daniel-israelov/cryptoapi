package com.example.cryptoapi.services;

import com.example.cryptoapi.assemblers.UserDtoAssembler;
import com.example.cryptoapi.dtos.UserDto;
import com.example.cryptoapi.entities.UserEntity;
import com.example.cryptoapi.entities.WalletEntity;
import com.example.cryptoapi.exceptions.UserAlreadyExistsException;
import com.example.cryptoapi.exceptions.UserIllegalAgeRangeException;
import com.example.cryptoapi.exceptions.UserNotFoundException;
import com.example.cryptoapi.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.*;
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
        log.info("Retrieved all users from service");
        return userDtoAssembler.toCollectionModel(dtoUsers);
    }

    public EntityModel<UserDto> getUserByIdentityNumber(Long identityNumber) {

        Optional<UserEntity> user = userRepository.findByIdentityNumber(identityNumber);
        if (user.isEmpty()) {
            throw new UserNotFoundException(identityNumber);
        }
        log.info("User with identityNumber = " + identityNumber + " returned in service = " + user);
        UserDto userDto = new UserDto(user.get());
        return userDtoAssembler.toModel(userDto);
    }

    public EntityModel<UserDto> findByFullName(String firstName, String lastName) {
        Optional<UserEntity> user = userRepository.findByFirstNameAndLastName(firstName, lastName);
        if (user.isEmpty()) {
            throw new UserNotFoundException(firstName, lastName);
        }
        log.info("Retrieved user by name '" + firstName + "' - '" + lastName + "' from service = " + user);
        return userDtoAssembler.toModel(new UserDto(user.get()));
    }

    public EntityModel<UserDto> createUser(@NotNull UserEntity userEntity) {
        if (userRepository.existsByIdentityNumber(userEntity.getIdentityNumber())) {
            throw new UserAlreadyExistsException(userEntity.getIdentityNumber());
        }
        log.info("Saving new user to database . . . Info = " + userEntity);
        userRepository.save(userEntity);
        return userDtoAssembler.toModel(new UserDto(userEntity));
    }

    public CollectionModel<EntityModel<UserDto>> findAllByGender(String gender) {
        if (!gender.equals("male") && !gender.equals("female")) {
            log.info("Invalid gender input in findAllByGender: '" + gender + "'.");
            return null;
        }
        List<UserEntity> allUsersByGender = userRepository.findAllByIsMale(gender.equals("male"));
        log.info("Users retrieved in findAllByGender('" + gender + "') = " + allUsersByGender);
        return userDtoAssembler.toCollectionModel(allUsersByGender.stream().map(UserDto::new).toList());
    }

    public CollectionModel<EntityModel<UserDto>> findAllByAgeRange(Integer from, Integer to) {
        if (from < 0 && to > 120) {
            log.info("Illegal min & max age input in findAllByAgeRange: " + from + ", " + to);
            throw new UserIllegalAgeRangeException(from,  to);
        } else if (from < 0) {
            log.info("Illegal min age input in findAllByAgeRange: " + from);
            throw new UserIllegalAgeRangeException(from);
        } else if (to > 120) {
            log.info("Illegal max age input in findAllByAgeRange: " + to);
            throw new UserIllegalAgeRangeException(to);
        }
        List<UserEntity> allUsersByAgeRange = userRepository.findAllByAgeRange(from, to);
        log.info("Users retrieved in findAllByAgeRange(" + from + ", "  + to + ") = " + allUsersByAgeRange);
        return userDtoAssembler.toCollectionModel(allUsersByAgeRange.stream().map(UserDto::new).toList());
    }

    public void deleteByIdentityNumber(Long identityNumber) {
        UserEntity user = userRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new UserNotFoundException(identityNumber));
        userRepository.deleteById(user.getId());
        log.info("User with id = " + identityNumber + " deleted in service = " + user);
    }

    public EntityModel<UserDto> updateUser(Long identityNumber, UserEntity userEntity) {
        log.info("UserEntity received in PUT request body = " + userEntity);
        UserEntity user = userRepository.findByIdentityNumber(identityNumber)
                .orElseThrow(() -> new UserNotFoundException(identityNumber));

        user.setFirstName(userEntity.getFirstName() == null ? user.getFirstName() : userEntity.getFirstName());
        user.setLastName(userEntity.getLastName() == null ? user.getLastName() : userEntity.getLastName());
        user.setAge(userEntity.getAge() == null ? user.getAge() : userEntity.getAge());
        user.setWalletEntities(userEntity.getWalletEntities().size() == 0 ? user.getWalletEntities() : userEntity.getWalletEntities());
        userRepository.save(user);

        log.info("Updated User info after PUT request = " + user);
        return userDtoAssembler.toModel(new UserDto(user));
    }

    public void removeUsersAttributionToWallet(WalletEntity wallet) {
        log.info("Removing connections between all relevant users to wallet = " + wallet);
        List<UserEntity> owners = wallet.getOwners().stream().toList();
        for (UserEntity owner : owners) {
            owner.disconnectWallets(wallet);
            userRepository.save(owner);
        }
    }
}
