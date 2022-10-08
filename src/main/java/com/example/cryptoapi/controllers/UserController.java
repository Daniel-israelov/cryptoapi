package com.example.cryptoapi.controllers;

import com.example.cryptoapi.dtos.UserDto;
import com.example.cryptoapi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{identityNumber}")
    public ResponseEntity<EntityModel<UserDto>> getUserByIdentityNumber(@PathVariable Long identityNumber) {
        return ResponseEntity.ok(userService.getUserByIdentityNumber(identityNumber));
    }
}
