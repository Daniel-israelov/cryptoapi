package com.example.cryptoapi.controllers;

import com.example.cryptoapi.dtos.UserDto;
import com.example.cryptoapi.entities.UserEntity;
import com.example.cryptoapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "User CRUD controller")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "GET all users")
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{identityNumber}")
    @Operation(summary = "GET user by Identity number")
    public ResponseEntity<EntityModel<UserDto>> getUserByIdentityNumber(@PathVariable Long identityNumber) {
        return ResponseEntity.ok(userService.getUserByIdentityNumber(identityNumber));
    }

    @GetMapping("/{firstName}/{lastName}")
    @Operation(summary = "GET user by his full name")
    public ResponseEntity<EntityModel<UserDto>> getUserByFullName(@PathVariable String firstName, @PathVariable String lastName) {
        return ResponseEntity.ok(userService.findByFullName(firstName, lastName));
    }

    @GetMapping("/bygender")
    @Operation(summary = "GET all users by gender")
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getUsersByGender(
            @RequestParam(required = false, defaultValue = "male") String gender) {

        return ResponseEntity.ok(userService.findAllByGender(gender));
    }

    @PostMapping("/createnew")
    @Operation(summary = "Create a new user")
    public ResponseEntity<?> createUser(@RequestBody UserEntity userEntity) {
        try {
            EntityModel<UserDto> user = userService.createUser(userEntity);
            return ResponseEntity
                    .created(new URI(user.getRequiredLink(IanaLinkRelations.SELF)
                            .getHref()))
                    .body(user);
        } catch (URISyntaxException uriSyntaxException) {
            return ResponseEntity.badRequest().body("Failed to create user = " + userEntity);
        }
    }
}
