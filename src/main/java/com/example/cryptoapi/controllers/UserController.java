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

    //ToDo - return collection (also update service and repo files)
    @GetMapping("/{firstName}/{lastName}")
    @Operation(summary = "GET user by his full name")
    public ResponseEntity<EntityModel<UserDto>> getUserByFullName(@PathVariable String firstName, @PathVariable String lastName) {
        return ResponseEntity.ok(userService.findByFullName(firstName, lastName));
    }

    @GetMapping("/gender")
    @Operation(summary = "GET all users by gender")
    public ResponseEntity<?> getUsersByGender(@RequestParam(required = false, defaultValue = "male") String gender) {

        CollectionModel<EntityModel<UserDto>> allUsersByGender = userService.findAllByGender(gender);
        if (allUsersByGender == null) {
            return ResponseEntity.badRequest().body("Invalid gender input: '" + gender + "'. Insert 'male' or 'female' only.");
        }
        return ResponseEntity.ok(userService.findAllByGender(gender));
    }

    @GetMapping("/age")
    @Operation(summary = "GET all users in a certain age range")
    public ResponseEntity<?> getUsersByAgeRange(@RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "120") Integer to) {

        return ResponseEntity.ok(userService.findAllByAgeRange(from, to));
    }

    @PostMapping("/createnew")
    @Operation(summary = "Create a new user")
    public ResponseEntity<?> createUser(@RequestBody UserEntity userEntity) {
        try {
            EntityModel<UserDto> user = userService.createUser(userEntity);
            return ResponseEntity
                    .created(new URI(user.getRequiredLink(IanaLinkRelations.SELF).getHref())).body(user);
        } catch (URISyntaxException uriSyntaxException) {
            return ResponseEntity.badRequest().body("Failed to create user = " + userEntity);
        }
    }

    @DeleteMapping("/{identityNumber}")
    @Operation(summary = "DELETE user by identity number")
    public ResponseEntity<String> deleteByIdentityNumber(@PathVariable Long identityNumber) {
        userService.deleteByIdentityNumber(identityNumber);
        return ResponseEntity.ok("User with identityNumber = " + identityNumber + " deleted");
    }

    @PutMapping("/{identityNumber}")
    @Operation(summary = "PUT request to update a user")
    public ResponseEntity<EntityModel<UserDto>> updateUser(@PathVariable Long identityNumber,
                                                           @RequestBody UserEntity userEntity) {
        return ResponseEntity.ok(userService.updateUser(identityNumber, userEntity));
    }
}
